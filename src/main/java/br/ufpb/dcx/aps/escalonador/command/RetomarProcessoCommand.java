package br.ufpb.dcx.aps.escalonador.command;

import br.ufpb.dcx.aps.escalonador.Escalonador;

public class RetomarProcessoCommand implements Command {

    private Escalonador escalonador;
    private String nomeProcesso;

    public RetomarProcessoCommand(String nomeProcesso){
        this.nomeProcesso = nomeProcesso;
    }

    @Override
    public void execute() {
        escalonador.retomarProcesso(nomeProcesso);
    }

    @Override
    public void setEscalonador(Escalonador escalonador) {
        this.escalonador = escalonador;
    }
}
