package br.com.fred.leitorPdf;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;

public class ReadPdf {

	public static void main(String[] args) throws IOException {

		System.out.println("Iniciando Processamento");
		File pdf = new File("C:\\Users\\Fred Farias\\Desktop\\Levantamentos UTI\\R_PACIENTE_POR_CID - 2019.pdf");
		File csv = new File(pdf.getParent() + System.getProperty("file.separator") + pdf.getName().replaceFirst("[.][^.]+$", "") + ".csv");
				
		PDDocument document = PDDocument.load(pdf);

		LayoutTextStripper stripper = new LayoutTextStripper();
		stripper.setSortByPosition(true);
		stripper.fixedCharWidth = 4; // e.g. 5

		String text = stripper.getText(document);
		
		String lines[] = text.split("\\r?\\n");
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(csv));
		
		bw.write("Atendimento;Data Atendimento;Nome;Cid;Descricao;Sexo;Idade;Convenio;Plano;Data Alta");
		bw.newLine();
		
		int count = 0;
		for (String line : lines) {
            Paciente paciente = parseLineToPaciente(line);
            
            if(paciente == null) {
            	continue;
            }
            
            if(count % 100 == 0) {
            	System.out.println(String.format("Processadas %s linhas", count));
            }
            
            if(paciente.getIdade() <= 19) {
            	bw.write(paciente.toString());
            	bw.newLine();
            }
            
            count++;
        }
        
        System.out.println("");
        
        
        bw.close();
        
        System.out.println("Finalizado");
        System.out.println("Verificar :" + csv);
        
    }
	
	public static Paciente parseLineToPaciente(String line) {
		
		if(line == null || line.isEmpty() || line.length() < 190) {
			return null;
		}
		
		try {
			if(Long.parseLong(line.substring(0, 13).trim()) <= 0) {
				return null;
			}
		}catch (Exception e) {
			return null;
		}
		
		Paciente paciente = new Paciente();
		paciente.atendimento = line.substring(0, 13).trim();
		paciente.dtAtendimento = line.substring(13, 25).trim();
		paciente.nome = organizaDado(line.substring(37, 71).trim());
		paciente.cid = line.substring(71, 78).trim();
		paciente.cidDescricao = organizaDado(line.substring(78, 111).trim());
		paciente.idade = organizaDado(line.substring(111, 124).trim());
		paciente.sexo = line.substring(124, 129).trim();
		paciente.convenio = organizaDado(line.substring(129, 148).trim());
		paciente.plano = organizaDado(line.substring(148, 167).trim());
		paciente.dtAlta = line.substring(181, 190).trim();
		
		return paciente;
	}
	
	public static String organizaDado(String campo) {
		return campo.replaceAll("^ +| +$|( )+", " ");
	}
}