package br.com.colbert.chartifacts.negocio.chartrun;

import java.io.Serializable;

import org.apache.commons.lang3.builder.Builder;
import org.jboss.weld.exceptions.IllegalStateException;

import br.com.colbert.chartifacts.dominio.chart.*;

/**
 * Classe que facilida a construção de instâncias de {@link VariacaoPosicao}.
 *
 * @author Thiago Colbert
 * @since 13/03/2015
 */
public class VariacaoPosicaoBuilder implements Serializable {

	private static final long serialVersionUID = 4804445477628463863L;

	private VariacaoPosicaoBuilder() {

	}

	/**
	 * Inicia a criação de uma nova variação de posição referente à estreia.
	 *
	 * @return objeto que precisa ter a posição da estreia definida
	 */
	public static VariacaoPosicaoEstreiaBuilder estreia() {
		return new VariacaoPosicaoEstreiaBuilder();
	}

	/**
	 * Inicia a criação de uma nova variação de posição referente a saída da parada.
	 *
	 * @return objeto que precisa ter a posição de saída definida
	 */
	public static VariacaoPosicaoSaidaBuilder saida() {
		return new VariacaoPosicaoSaidaBuilder();
	}

	/**
	 * Inicia a criação de uma nova variação de posição referente a retorno.
	 *
	 * @return objeto que precisa ter a posição de retorno definida
	 */
	public static VariacaoPosicaoRetornoBuilder retorno() {
		return new VariacaoPosicaoRetornoBuilder();
	}

	/**
	 * Inicia a criação de uma nova variação de posição referente a subida de posições.
	 *
	 * @return objeto que precisa ter as posições definidas
	 */
	public static VariacaoPosicaoSubidaBuilder subida() {
		return new VariacaoPosicaoSubidaBuilder();
	}

	/**
	 * Inicia a criação de uma nova variação de posição referente a queda de posições.
	 *
	 * @return objeto que precisa ter as posições definidas
	 */
	public static VariacaoPosicaoComumBuilder queda() {
		return new VariacaoPosicaoQuedaBuilder();
	}

	/**
	 * Verifica se uma posição é válida.
	 *
	 * @param posicao
	 *            posição a ser verificado
	 * @return a posição, caso seja válida
	 * @throws IllegalArgumentException
	 *             caso a posição informada seja inválida
	 */
	protected PosicaoChart validar(PosicaoChart posicao) {
		return posicao;
	}

	/**
	 * Cria uma variação de posição com os valores informados.
	 *
	 * @param posicaoA
	 *            a primeira posição
	 * @param posicaoB
	 *            a segunda posição
	 * @param tipoVariacao
	 *            o tipo de variação
	 * @return a instância criada
	 */
	protected VariacaoPosicao nova(PosicaoChart posicaoA, PosicaoChart posicaoB, TipoVariacaoPosicao tipoVariacao) {
		return new VariacaoPosicao(posicaoA, posicaoB, tipoVariacao);
	}

	static abstract class VariacaoPrimeiraPosicaoUnicaBuilder extends VariacaoPosicaoBuilder implements Builder<VariacaoPosicao>, Serializable {

		private static final long serialVersionUID = 8866348771889240163L;

		private final TipoVariacaoPosicao tipoVariacao;

		private PosicaoChart posicao;

		VariacaoPrimeiraPosicaoUnicaBuilder(TipoVariacaoPosicao tipoVariacao) {
			this.tipoVariacao = tipoVariacao;
		}

		/**
		 * Define a posição.
		 *
		 * @param posicao
		 *            a posição
		 * @return <code>this</code>, para chamadas encadeadas
		 * @throws IllegalArgumentException
		 *             caso a posição informada seja inválida
		 */
		public VariacaoPrimeiraPosicaoUnicaBuilder em(PosicaoChart posicao) {
			this.posicao = validar(posicao);
			return this;
		}

		@Override
		protected PosicaoChart validar(PosicaoChart posicao) {
			if (posicao.isAusencia()) {
				throw new IllegalArgumentException("A posição não pode ser ausência");
			} else {
				return posicao;
			}
		}

		@Override
		public VariacaoPosicao build() {
			return nova(posicao, PosicaoChart.AUSENCIA, tipoVariacao);
		}
	}

	static abstract class VariacaoSegundaPosicaoUnicaBuilder extends VariacaoPosicaoBuilder implements Builder<VariacaoPosicao>, Serializable {

		private static final long serialVersionUID = 8866348771889240163L;

		private final TipoVariacaoPosicao tipoVariacao;

		private PosicaoChart posicao;

		VariacaoSegundaPosicaoUnicaBuilder(TipoVariacaoPosicao tipoVariacao) {
			this.tipoVariacao = tipoVariacao;
		}

		/**
		 * Define a posição.
		 *
		 * @param posicao
		 *            a posição
		 * @return <code>this</code>, para chamadas encadeadas
		 * @throws IllegalArgumentException
		 *             caso a posição informada seja inválida
		 */
		public VariacaoSegundaPosicaoUnicaBuilder em(PosicaoChart posicao) {
			this.posicao = validar(posicao);
			return this;
		}

		@Override
		protected PosicaoChart validar(PosicaoChart posicao) {
			if (posicao.isAusencia()) {
				throw new IllegalArgumentException("A posição não pode ser ausência");
			} else {
				return posicao;
			}
		}

		@Override
		public VariacaoPosicao build() {
			return nova(PosicaoChart.AUSENCIA, posicao, tipoVariacao);
		}
	}

	static abstract class VariacaoPosicaoComumBuilder extends VariacaoPosicaoBuilder implements Builder<VariacaoPosicao>, Serializable {

		private static final long serialVersionUID = 8866348771889240163L;

		private final TipoVariacaoPosicao tipoVariacao;

		private PosicaoChart posicaoA;
		private PosicaoChart posicaoB;

		VariacaoPosicaoComumBuilder(TipoVariacaoPosicao tipoVariacao) {
			this.tipoVariacao = tipoVariacao;
		}

		/**
		 * Define a posição inicial.
		 *
		 * @param posicaoA
		 *            a posição inicial
		 * @return <code>this</code>, para chamadas encadeadas
		 * @throws IllegalArgumentException
		 *             caso a posição informada seja inválida
		 */
		public VariacaoPosicaoComumBuilder de(PosicaoChart posicaoA) {
			this.posicaoA = validar(posicaoA);
			return this;
		}

		/**
		 * Define a posição final.
		 *
		 * @param posicaoB
		 *            o número da posição final
		 * @return <code>this</code>, para chamadas encadeadas
		 * @throws IllegalArgumentException
		 *             caso a posição informada seja inválida
		 */
		public VariacaoPosicaoComumBuilder para(PosicaoChart posicaoB) {
			this.posicaoB = validar(posicaoB);
			return this;
		}

		@Override
		protected PosicaoChart validar(PosicaoChart posicao) {
			if (posicaoA != null && posicaoA.isPresenca() && posicaoB != null && posicaoB.isPresenca()
					&& !(Math.signum(posicaoA.compareTo(posicaoB)) == tipoVariacao.delta())) {
				throw new IllegalArgumentException("Definição inválida para este tipo de variação (" + tipoVariacao + "): [" + posicaoA + '-' + posicaoB + "]");
			} else {
				return posicao;
			}
		}

		@Override
		public VariacaoPosicao build() {
			if (posicaoA.isAusencia() || posicaoB.isAusencia()) {
				throw new IllegalStateException("Ambas as posições precisam ser definidas");
			}

			return nova(posicaoA, posicaoB, tipoVariacao);
		}
	}

	/**
	 * Classe que facilida a construção de instâncias de {@link VariacaoPosicao} referentes à estreia em uma parada.
	 *
	 * @author Thiago Colbert
	 * @since 13/03/2015
	 */
	public static class VariacaoPosicaoEstreiaBuilder extends VariacaoSegundaPosicaoUnicaBuilder {

		private static final long serialVersionUID = 6618689681762072504L;

		VariacaoPosicaoEstreiaBuilder() {
			super(TipoVariacaoPosicao.ESTREIA);
		}
	}

	/**
	 * Classe que facilida a construção de instâncias de {@link VariacaoPosicao} referentes a retorno à uma parada.
	 *
	 * @author Thiago Colbert
	 * @since 13/03/2015
	 */
	public static class VariacaoPosicaoRetornoBuilder extends VariacaoSegundaPosicaoUnicaBuilder {

		private static final long serialVersionUID = 6618689681762072504L;

		VariacaoPosicaoRetornoBuilder() {
			super(TipoVariacaoPosicao.RETORNO);
		}
	}

	/**
	 * Classe que facilida a construção de instâncias de {@link VariacaoPosicao} referentes a saída de uma parada.
	 *
	 * @author Thiago Colbert
	 * @since 13/03/2015
	 */
	public static class VariacaoPosicaoSaidaBuilder extends VariacaoPrimeiraPosicaoUnicaBuilder {

		private static final long serialVersionUID = 6618689681762072504L;

		VariacaoPosicaoSaidaBuilder() {
			super(TipoVariacaoPosicao.SAIDA);
		}
	}

	/**
	 * Classe que facilida a construção de instâncias de {@link VariacaoPosicao} referentes a subida de posição em uma parada.
	 *
	 * @author Thiago Colbert
	 * @since 13/03/2015
	 */
	public static class VariacaoPosicaoSubidaBuilder extends VariacaoPosicaoComumBuilder {

		private static final long serialVersionUID = -9199829919892827308L;

		VariacaoPosicaoSubidaBuilder() {
			super(TipoVariacaoPosicao.SUBIDA);
		}
	}

	/**
	 * Classe que facilida a construção de instâncias de {@link VariacaoPosicao} referentes a queda de posição em uma parada.
	 *
	 * @author Thiago Colbert
	 * @since 13/03/2015
	 */
	public static class VariacaoPosicaoQuedaBuilder extends VariacaoPosicaoComumBuilder {

		private static final long serialVersionUID = -9199829919892827308L;

		VariacaoPosicaoQuedaBuilder() {
			super(TipoVariacaoPosicao.QUEDA);
		}
	}
}
