package compilador.lexico;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import compilador.lexico.estruturas.AFD;
import compilador.lexico.estruturas.Estado;
import compilador.lexico.estruturas.Transicao;


public class MontaAFD {
	
	private String arquivoXML = "automato.xml";
	
	public MontaAFD(){
		
	}
	
	public boolean executa(AFD automato){
		
		parseXML(this.arquivoXML, automato);
		
		return true;
	}
	
	
	public boolean parseXML(String arquivoXML, AFD automato){
		
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(arquivoXML);
			
			XPathFactory factoryXPath = XPathFactory.newInstance();
			XPath xpath = factoryXPath.newXPath();
			
			// Pega todos os estados
			XPathExpression expr = xpath.compile("//estado");
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			
			automato.setNumEstados(nodes.getLength());
			
			// Adiciona cada estado no AFD
			for (int i = 0; i < nodes.getLength(); i++) {
				
				// Recupera o id
				expr = xpath.compile("//id/text()");
				result = expr.evaluate(nodes.item(i), XPathConstants.NODE);
				Node node =  (Node) result;
				int id = Integer.parseInt(node.getNodeValue()) ;
				
				// Recupera se é final ou não
				expr = xpath.compile("//final/text()");
				result = expr.evaluate(nodes.item(i), XPathConstants.NODE);
				node =  (Node) result;
				boolean aceitacao = Boolean.parseBoolean(node.getNodeValue());
				
				// Cria o estado
				Estado estado = new Estado(id, aceitacao);
				
				// Recupera as transições
				expr = xpath.compile("//transicao");
				result = expr.evaluate(nodes.item(i), XPathConstants.NODESET);
				NodeList nodesTransicao = (NodeList) result;
				
				// Seta o número de transições existente para inicializar o 
				// vetor de transições
				estado.setNumTransicoes(nodesTransicao.getLength());
				
				// Adiciona cada transição ao estado
				for (int j = 0; j < nodesTransicao.getLength(); j++) {
					
					// Recupera as entradas
					expr = xpath.compile("//entradas/text()");
					result = expr.evaluate(nodesTransicao.item(j), XPathConstants.NODE);
					node =  (Node) result;
					String entrada = node.getNodeValue();
					
					// Recupera o id do próximo estado
					expr = xpath.compile("//proximo/text()");
					result = expr.evaluate(nodesTransicao.item(j), XPathConstants.NODE);
					node =  (Node) result;
					int proximo = Integer.parseInt(node.getNodeValue());
					
					// Cria a transição
					Transicao transicao = new Transicao(proximo, entrada);
					
					// Adiciona a transição ao estado
					estado.adicionaTransicao(transicao, j);
				}
				
				automato.adicionaEstado(estado, i);
				
				System.out.println(nodes.item(i).getNodeValue()); 
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

}
