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
		// Cria o automato a partir do aquivo XML.
		return parseXML(this.arquivoXML, automato);
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
				
				// Recupera se � final ou n�o
				expr = xpath.compile("//final/text()");
				result = expr.evaluate(nodes.item(i), XPathConstants.NODE);
				node =  (Node) result;
				boolean aceitacao = Boolean.parseBoolean(node.getNodeValue());
				
				// Recupera o tipo
				expr = xpath.compile("//tipo/text()");
				result = expr.evaluate(nodes.item(i), XPathConstants.NODE);
				node =  (Node) result;
				int tipo = Integer.parseInt(node.getNodeValue());
				
				// Cria o estado
				Estado estado = new Estado(id, aceitacao, tipo);
				
				// Recupera as transi��es
				expr = xpath.compile("//transicao");
				result = expr.evaluate(nodes.item(i), XPathConstants.NODESET);
				NodeList nodesTransicao = (NodeList) result;
				
				// Seta o n�mero de transi��es existente para inicializar o 
				// vetor de transi��es
				estado.setNumTransicoes(nodesTransicao.getLength());
				
				// Adiciona cada transi��o ao estado
				for (int j = 0; j < nodesTransicao.getLength(); j++) {
					
					// Recupera as entradas
					expr = xpath.compile("//entradas/text()");
					result = expr.evaluate(nodesTransicao.item(j), XPathConstants.NODE);
					node =  (Node) result;
					String entrada = node.getNodeValue();
					
					// Recupera o id do pr�ximo estado
					expr = xpath.compile("//proximo/text()");
					result = expr.evaluate(nodesTransicao.item(j), XPathConstants.NODE);
					node =  (Node) result;
					int proximo = Integer.parseInt(node.getNodeValue());
					
					// Cria a transi��o
					Transicao transicao = new Transicao(proximo, entrada);
					
					// Adiciona a transi��o ao estado
					estado.adicionaTransicao(transicao, j);
				}
				
				// Adiciona o estado no automato
				automato.adicionaEstado(estado, i);
				
				//System.out.println(nodes.item(i).getNodeValue()); 
			}
			return true;
			
		}catch(Exception e){
			//e.printStackTrace();
			System.out.println("[ERRO] Erro ao fazer o parser do arquivo XML");
			return false;
		}
	}

}
