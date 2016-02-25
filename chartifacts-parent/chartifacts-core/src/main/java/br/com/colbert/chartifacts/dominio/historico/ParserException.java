package br.com.colbert.chartifacts.dominio.historico;

/**
 * Exceção lançada quando ocorre algum erro ao fazer o <em>parse</em> de determinado conteúdo.
 *
 * @author Thiago Colbert
 * @since 15/03/2015
 */
public class ParserException extends Exception {

	private static final long serialVersionUID = -2425379594070174773L;

	/**
	 * 
	 * @param message
	 * @param cause
	 */
	public ParserException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 
	 * @param message
	 */
	public ParserException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param cause
	 */
	public ParserException(Throwable cause) {
		super(cause);
	}
}
