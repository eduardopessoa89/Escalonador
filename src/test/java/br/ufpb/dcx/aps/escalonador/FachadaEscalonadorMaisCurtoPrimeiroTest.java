package br.ufpb.dcx.aps.escalonador;

import static br.ufpb.dcx.aps.escalonador.TestHelper.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import br.ufpb.dcx.aps.escalonador.comando.ComandoTick;

public class FachadaEscalonadorMaisCurtoPrimeiroTest {
	
	private FachadaEscalonador fachada;
	
	@BeforeEach
	public void inicializar() {
		fachada = new FachadaEscalonador(TipoEscalonador.MaisCurtoPrimeiro);
	}

    @Test
	public void t01_statusAposCriacao() {
		checaStatus(fachada, TipoEscalonador.MaisCurtoPrimeiro, 0, 0);
	}

   	@Test
	public void t02_avancarTempo() {
		fachada.execute(new ComandoTick(fachada));
		checaStatus(fachada, TipoEscalonador.MaisCurtoPrimeiro, 0, 1);
	}

    @Test
	public void t03_processoTerminaPorSiSo() {
		fachada.adicionarProcessoTempoFixo("P1", 2);
		checaStatusFila(fachada, TipoEscalonador.MaisCurtoPrimeiro, 0, 0, "P1");
		
		fachada.execute(new ComandoTick(fachada));
		checaStatusRodando(fachada, TipoEscalonador.MaisCurtoPrimeiro, 0, 1, "P1");
		
		fachada.execute(new ComandoTick(fachada));
		checaStatusRodando(fachada, TipoEscalonador.MaisCurtoPrimeiro, 0, 2, "P1");
		
		//Acaba a duração do processo e libera a CPU 
		fachada.execute(new ComandoTick(fachada));
		checaStatus(fachada, TipoEscalonador.MaisCurtoPrimeiro, 0, 3);
	}

	@Test
	public void t04_doisProcessosIniciaPeloMenor() {
		fachada.adicionarProcessoTempoFixo("P1", 3);
		fachada.adicionarProcessoTempoFixo("P2", 2);

		fachada.execute(new ComandoTick(fachada));
		checaStatusRodandoFila(fachada, TipoEscalonador.MaisCurtoPrimeiro, 0, 1, "P2", "P1");

		fachada.execute(new ComandoTick(fachada));
		checaStatusRodandoFila(fachada, TipoEscalonador.MaisCurtoPrimeiro, 0, 2, "P2", "P1");

		fachada.execute(new ComandoTick(fachada));
		checaStatusRodando(fachada, TipoEscalonador.MaisCurtoPrimeiro, 0, 3, "P1");

		fachada.execute(new ComandoTick(fachada));
		checaStatusRodando(fachada, TipoEscalonador.MaisCurtoPrimeiro, 0, 4, "P1");
		
		fachada.execute(new ComandoTick(fachada));
		checaStatusRodando(fachada, TipoEscalonador.MaisCurtoPrimeiro, 0, 5, "P1");
		
		fachada.execute(new ComandoTick(fachada));
		checaStatus(fachada, TipoEscalonador.MaisCurtoPrimeiro, 0, 6);
	}
	
	@Test
	public void t05_tresProcessosDesempatePelaOrdemDeAdicao() {
		fachada.adicionarProcessoTempoFixo("P1", 3);
		fachada.adicionarProcessoTempoFixo("P2", 2);
		fachada.adicionarProcessoTempoFixo("P3", 3);

		fachada.execute(new ComandoTick(fachada));
		checaStatusRodandoFila(fachada, TipoEscalonador.MaisCurtoPrimeiro, 0, 1, "P2", "P1", "P3");

		ticks(fachada, 2);
		checaStatusRodandoFila(fachada, TipoEscalonador.MaisCurtoPrimeiro, 0, 3, "P1", "P3");

		ticks(fachada, 3);
		checaStatusRodando(fachada, TipoEscalonador.MaisCurtoPrimeiro, 0, 6, "P3");

		ticks(fachada, 3);
		checaStatus(fachada, TipoEscalonador.MaisCurtoPrimeiro, 0, 9);
	}
	
	@Test
	public void t06_tresProcessosInicioDiferente() {
		fachada.adicionarProcessoTempoFixo("P1", 2);

		fachada.execute(new ComandoTick(fachada));
		checaStatusRodando(fachada, TipoEscalonador.MaisCurtoPrimeiro, 0, 1, "P1");

		fachada.adicionarProcessoTempoFixo("P2", 3);
		fachada.adicionarProcessoTempoFixo("P3", 2);

		fachada.execute(new ComandoTick(fachada));
		checaStatusRodandoFila(fachada, TipoEscalonador.MaisCurtoPrimeiro, 0, 2, "P1", "P3", "P2");

		fachada.execute(new ComandoTick(fachada));
		checaStatusRodandoFila(fachada, TipoEscalonador.MaisCurtoPrimeiro, 0, 3, "P3", "P2");
				
		ticks(fachada, 2);
		checaStatusRodando(fachada, TipoEscalonador.MaisCurtoPrimeiro, 0, 5, "P2");

		ticks(fachada, 3);
		checaStatus(fachada, TipoEscalonador.MaisCurtoPrimeiro, 0, 8);
	}
	
	@Test
	public void t07_tresProcessosInicioDiferente() {
		fachada.adicionarProcessoTempoFixo("P1", 2);

		fachada.execute(new ComandoTick(fachada));
		checaStatusRodando(fachada, TipoEscalonador.MaisCurtoPrimeiro, 0, 1, "P1");

		fachada.adicionarProcessoTempoFixo("P2", 3);

		fachada.execute(new ComandoTick(fachada));
		checaStatusRodandoFila(fachada, TipoEscalonador.MaisCurtoPrimeiro, 0, 2, "P1", "P2");

		fachada.adicionarProcessoTempoFixo("P3", 2);
		
		fachada.execute(new ComandoTick(fachada));
		checaStatusRodandoFila(fachada, TipoEscalonador.MaisCurtoPrimeiro, 0, 3, "P3", "P2");
				
		ticks(fachada, 2);
		checaStatusRodando(fachada, TipoEscalonador.MaisCurtoPrimeiro, 0, 5, "P2");

		ticks(fachada, 3);
		checaStatus(fachada, TipoEscalonador.MaisCurtoPrimeiro, 0, 8);
	}

	@Test
	public void t08_tresProcessosAdicionarMenorNoMeio() {
		fachada.adicionarProcessoTempoFixo("P1", 2);

		fachada.execute(new ComandoTick(fachada));
		checaStatusRodando(fachada, TipoEscalonador.MaisCurtoPrimeiro, 0, 1, "P1");

		fachada.adicionarProcessoTempoFixo("P2", 1);

		fachada.execute(new ComandoTick(fachada));
		checaStatusRodandoFila(fachada, TipoEscalonador.MaisCurtoPrimeiro, 0, 2, "P1", "P2");

		fachada.adicionarProcessoTempoFixo("P3", 2);
		
		fachada.execute(new ComandoTick(fachada));
		checaStatusRodandoFila(fachada, TipoEscalonador.MaisCurtoPrimeiro, 0, 3, "P2", "P3");
				
		fachada.execute(new ComandoTick(fachada));
		checaStatusRodando(fachada, TipoEscalonador.MaisCurtoPrimeiro, 0, 4, "P3");

		ticks(fachada, 2);
		checaStatus(fachada, TipoEscalonador.MaisCurtoPrimeiro, 0, 6);
	}
	
	@Test
	public void t09_intervaloEntreProcessos() {
		fachada.adicionarProcessoTempoFixo("P1", 3);

		fachada.execute(new ComandoTick(fachada));
		checaStatusRodando(fachada, TipoEscalonador.MaisCurtoPrimeiro, 0, 1, "P1");

		ticks(fachada, 6);
		checaStatus(fachada, TipoEscalonador.MaisCurtoPrimeiro, 0, 7);

		
		fachada.adicionarProcessoTempoFixo("P2", 2);
		checaStatusFila(fachada, TipoEscalonador.MaisCurtoPrimeiro, 0, 7, "P2");

		fachada.execute(new ComandoTick(fachada));
		checaStatusRodando(fachada, TipoEscalonador.MaisCurtoPrimeiro, 0, 8, "P2");

		ticks(fachada, 3);
		checaStatus(fachada, TipoEscalonador.MaisCurtoPrimeiro, 0, 11);
	}
	

	@Test
	public void t10_validacoes() {
		fachada.adicionarProcessoTempoFixo("P", 1);
		assertThrows(EscalonadorException.class, () -> fachada.adicionarProcessoTempoFixo("P", 2),
				"Já existe um processo com o nome P" );

		assertThrows(EscalonadorException.class, () -> fachada.adicionarProcessoTempoFixo(null, 1), 
				"O nome do processo é obrigatório" );

		assertThrows(EscalonadorException.class, () -> fachada.adicionarProcessoTempoFixo("Q", 0), 
				"A duração do processo deve ser maior que zero" );

		assertThrows(EscalonadorException.class, () -> fachada.adicionarProcessoTempoFixo("Q", -1), 
				"A duração do processo deve ser maior que zero" );

		assertThrows(EscalonadorException.class, () -> fachada.adicionarProcesso("P"), 
				"O Escalonador Mais Curto Primeiro exige que todos os processos tenham uma duração definida na adição" );

		assertThrows(EscalonadorException.class, () -> fachada.adicionarProcesso("P", 2), 
				"O Escalonador Mais Curto Primeiro exige que todos os processos tenham uma duração definida na adição" );

	}

}
