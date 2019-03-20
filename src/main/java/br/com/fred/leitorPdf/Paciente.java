package br.com.fred.leitorPdf;

import java.util.Arrays;

public class Paciente {

	public String atendimento;
	public String dtAtendimento;
	public String nome;
	public String cid;
	public String cidDescricao;
	public String sexo;
	public String idade;
	public String convenio;
	public String plano;
	public String dtAlta;
	
	public int getIdade() {
		
		int idx = this.idade.indexOf("a");
		
		if(idx < 0) {
			return 0;
		}
		
		return Integer.parseInt(this.idade.substring(0, idx));
	}
	
	@Override
	public String toString() {
		return String.join(";", Arrays.asList(atendimento, dtAtendimento, nome, cid, cidDescricao, sexo, "" + getIdade(), convenio, plano, dtAlta));
	}
}
