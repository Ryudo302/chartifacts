package br.com.colbert.chartifacts.infraestrutura.io;

import java.awt.*;
import java.beans.Beans;
import java.io.Serializable;
import java.net.URL;
import java.util.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.swing.ImageIcon;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;

/**
 * Um repositório de imagens.
 * 
 * @author ThiagoColbert
 * @since 13 de fev de 2016
 */
@ApplicationScoped
public class ImagensRepository implements Serializable {

	private static final long serialVersionUID = -974240032317648745L;

	@Inject
	private transient Logger logger;

	private final Map<String, ImageIcon> cacheIcones;

	/**
	 * Cria uma nova instância.
	 */
	public ImagensRepository() {
		cacheIcones = new HashMap<>();
	}

	/**
	 * Carrega uma imagem a partir do sistema de arquivos.
	 * 
	 * @param caminhoArquivo
	 *            caminho do arquivo de imagem
	 * @param tamanhoOptional
	 *            o tamanho desejado da imagem - pode ser vazio para que seja utilizado o tamanho original
	 * @return a imagem carregada (pode estar vazio caso não exista)
	 * @throws NullPointerException
	 *             caso qualquer parâmetro seja <code>null</code>
	 * @throws IllegalArgumentException
	 *             caso o caminho informado seja uma String vazia
	 */
	public Optional<Image> recuperarImagem(String caminhoArquivo, Optional<Dimension> tamanhoOptional) {
		Optional<ImageIcon> iconeOptional = recuperarIcone(caminhoArquivo, tamanhoOptional);
		return iconeOptional.isPresent() ? Optional.of(iconeOptional.get().getImage()) : Optional.empty();
	}

	/**
	 * Carrega um ícone.
	 * 
	 * @param caminhoArquivo
	 *            caminho do arquivo de imagem do ícone
	 * @param tamanhoOptional
	 *            o tamanho desejado da imagem - pode ser vazio para que seja utilizado o tamanho original
	 * @return o ícone carregado (pode estar vazio caso não exista)
	 * @throws NullPointerException
	 *             caso qualquer parâmetro seja <code>null</code>
	 * @throws IllegalArgumentException
	 *             caso o caminho informado seja uma String vazia
	 */
	public Optional<ImageIcon> recuperarIcone(String caminhoArquivo, Optional<Dimension> tamanhoOptional) {
		Validate.notBlank(caminhoArquivo, "caminhoArquivo");
		Optional<ImageIcon> iconeOptional = Optional.ofNullable(cacheIcones.get(caminhoArquivo));

		if (!iconeOptional.isPresent()) {
			logger.debug("Carregando imagem a partir do arquivo: {}", caminhoArquivo);

			URL resource = Thread.currentThread().getContextClassLoader().getResource(caminhoArquivo);
			ImageIcon icone = Beans.isDesignTime() || resource == null ? null : new ImageIcon(resource);

			cacheIcones.put(caminhoArquivo, icone);
			iconeOptional = Optional.ofNullable(icone);
		}

		if (iconeOptional.isPresent() && Objects.requireNonNull(tamanhoOptional, "tamanho").isPresent()) {
			Dimension tamanho = tamanhoOptional.get();
			logger.debug("Redimensionando imagem para: {}", tamanho);
			iconeOptional = Optional
					.of(new ImageIcon(iconeOptional.get().getImage().getScaledInstance(tamanho.width, tamanho.height, java.awt.Image.SCALE_SMOOTH)));
		}

		return iconeOptional;
	}
}
