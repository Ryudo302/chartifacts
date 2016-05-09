package br.com.colbert.chartifacts;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.*;
import java.util.stream.Stream;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.any23.encoding.*;
import org.apache.commons.lang3.math.NumberUtils;
import org.jboss.weld.environment.se.StartMain;
import org.jboss.weld.environment.se.events.ContainerInitialized;
import org.slf4j.Logger;

import br.com.colbert.chartifacts.dominio.chart.Chart;
import br.com.colbert.chartifacts.dominio.historico.CalculadoraPontos;
import br.com.colbert.chartifacts.infraestrutura.properties.PropertiesFilesResolver;
import br.com.colbert.chartifacts.negocio.parser.*;

/**
 * TODO
 *
 * @author ThiagoColbert
 * @since 8 de mai de 2016
 */
public class ChartParserMain {

	@Inject
	private transient Logger logger;
	@Inject
	private transient ChartParser chartParser;

	public static void main(String[] args) {
		System.setProperty(PropertiesFilesResolver.SYSTEM_PROPERTY_ARQUIVOS, "parser.properties");
		StartMain.main(args);
	}

	public void start(/*@Observes*/ ContainerInitialized event) throws URISyntaxException, IOException, ParserException {
		chartParser.setNumeroParadaPattern(Pattern.compile("[\\w\\s]+ #(\\d+) .*"));
		chartParser.setPosicaoPattern(Pattern.compile("(\\d{1,2}).? .+"));
		chartParser.setCalculadoraPontos(new CalculadoraPontos(20));

		Map<Integer, Stream<Chart>> chartsPorAno = new TreeMap<>();
		Files.list(Paths.get("D:" + File.separatorChar, "Users", "ThiagoColbert", "Documents", "Paradas Musicais", "Hit Parade TL"))
				.filter(diretorioDeAno()).parallel().forEach(diretorioPath -> {
					chartsPorAno.put(Integer.valueOf(diretorioPath.getFileName().toString()),
							lerArquivosDoDiretorio(diretorioPath, new TikaEncodingDetector()));
				});

		logger.info("Paradas processadas: {}", chartsPorAno);
	}

	private Predicate<? super Path> diretorioDeAno() {
		return diretorioPath -> NumberUtils.isDigits(diretorioPath.getFileName().toString());
	}

	private Stream<Chart> lerArquivosDoDiretorio(Path diretorioPath, EncodingDetector encodingDetector) {
		logger.info("Diretório: {}", diretorioPath.getFileName());
		String nomeArquivoRegex = "Hit Parade TL - (\\d{2}\\.\\d{2}\\.\\d{2})\\.txt";
		Pattern nomeArquivoPattern = Pattern.compile(nomeArquivoRegex);
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy");

		try (Stream<Path> arquivosStream = Files.list(diretorioPath)) {
			logger.debug("{} arquivos", arquivosStream.count());
			return Files.list(diretorioPath).filter(arquivosCujoNomeMatches(nomeArquivoRegex))
					.sorted(ordenacaoPorDataParada(nomeArquivoPattern, dateTimeFormatter))
					.map(arquivoPath -> lerArquivo(arquivoPath, encodingDetector));
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	private Predicate<? super Path> arquivosCujoNomeMatches(String regex) {
		return path -> path.toFile().getName().matches(regex);
	}

	private Comparator<? super Path> ordenacaoPorDataParada(Pattern nomeArquivoPattern, DateTimeFormatter dateTimeFormatter) {
		return (path1, path2) -> {
			Matcher matcher1 = nomeArquivoPattern.matcher(path1.toFile().getName());
			Matcher matcher2 = nomeArquivoPattern.matcher(path2.toFile().getName());

			if (matcher1.find() && matcher2.find()) {
				return LocalDate.parse(matcher1.group(1), dateTimeFormatter).compareTo(LocalDate.parse(matcher2.group(1), dateTimeFormatter));
			} else {
				return 0;
			}
		};
	}

	private Chart lerArquivo(Path arquivoPath, EncodingDetector encodingDetector) {
		logger.debug("Arquivo: {}", arquivoPath.getFileName());
		try (InputStream inputStream = Files.newInputStream(arquivoPath)) {
			Chart chart = chartParser.parse(Files.readAllLines(arquivoPath, Charset.forName(encodingDetector.guessEncoding(inputStream))), 20);
			logger.debug("Chart nº {}", chart.getNumero());
			return chart;
		} catch (IOException | ParserException exception) {
			throw new RuntimeException(exception);
		}
	}
}
