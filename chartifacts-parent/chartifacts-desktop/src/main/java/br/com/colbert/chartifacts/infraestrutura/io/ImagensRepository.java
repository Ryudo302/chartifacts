package br.com.colbert.chartifacts.infraestrutura.io;

import java.beans.Beans;
import java.io.Serializable;
import java.util.*;

import javax.enterprise.context.ApplicationScoped;
import javax.swing.ImageIcon;

/**
 * Um repositório de imagens.
 * 
 * @author ThiagoColbert
 * @since 13 de fev de 2016
 */
@ApplicationScoped
public class ImagensRepository implements Serializable {

	private static final long serialVersionUID = -974240032317648745L;

	private final Map<String, ImageIcon> cacheIcones;

	/**
	 * Cria uma nova instância.
	 */
	public ImagensRepository() {
		cacheIcones = new HashMap<>();
	}

	/**
	 * Carrega um ícone.
	 * 
	 * @param caminhoArquivo
	 *            caminho do arquivo de imagem do ícone
	 * @return o ícone carregado (pode ser <code>null</code> caso não exista)
	 */
	public ImageIcon recuperarIcone(String caminhoArquivo) {
		ImageIcon icone = cacheIcones.get(caminhoArquivo);

		if (icone == null) {
			icone = Beans.isDesignTime() ? null : new ImageIcon(Thread.currentThread().getContextClassLoader().getResource(caminhoArquivo));
			cacheIcones.put(caminhoArquivo, icone);
		}

		return icone;
	}
}
