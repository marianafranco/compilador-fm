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
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

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
			
			// Para fazer a validacao do XML atraves da DTD
			factory.setValidating(true);	
			ErrorHandler handler = new MeuErrorHandler();

			DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setErrorHandler(handler);
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
				expr = xpath.compile("./id/text()");
				result = expr.evaluate(nodes.item(i), XPathConstants.NODE);
				Node node =  (Node) result;
				int id = Integer.parseInt(node.getNodeValue()) ;
				System.out.println("Estado ID: " + id);
				
				// Recupera se é final ou não
				expr = xpath.compile("./final/text()");
				result = expr.evaluate(nodes.item(i), XPathConstants.NODE);
				node =  (Node) result;
				boolean aceitacao = Boolean.parseBoolean(node.getNodeValue());
				System.out.println("\tFINAL: " + aceitacao);
				
				// Recupera o tipo
				expr = xpath.compile("./tipo/text()");
				result = expr.evaluate(nodes.item(i), XPathConstants.NODE);
				node =  (Node) result;
				int tipo = Integer.parseInt(node.getNodeValue());
				System.out.println("\tTIPO: " + tipo);
				
				// Cria o estado
				Estado estado = new Estado(id, aceitacao, tipo);
				
				// Recupera as transições
				expr = xpath.compile("./transicao");
				result = expr.evaluate(nodes.item(i), XPathConstants.NODESET);
				NodeList nodesTransicao = (NodeList) result;
				
				// Seta o número de transições existente para inicializar o 
				// vetor de transições
				estado.setNumTransicoes(nodesTransicao.getLength());
				
				// Adiciona cada transição ao estado
				for (int j = 0; j < nodesTransicao.getLength(); j++) {
					
					System.out.println("\tTransicao:");
					
					// Recupera as entradas
					expr = xpath.compile("./entradas/text()");
					result = expr.evaluate(nodesTransicao.item(j), XPathConstants.NODE);
					node =  (Node) result;
					String entrada = node.getNodeValue();
					System.out.println("\t\tENTRADA: " + entrada);
					
					// Recupera o id do próximo estado
					expr = xpath.compile("./proximo/text()");
					result = expr.evaluate(nodesTransicao.item(j), XPathConstants.NODE);
					node =  (Node) result;
					int proximo = Integer.parseInt(node.getNodeValue());
					System.out.println("\t\tPROXIMO: " + proximo);
					
					// Cria a transição
					Transicao transicao = new Transicao(proximo, entrada);
					
					// Adiciona a transição ao estado
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
	
	private static class MeuErrorHandler implements ErrorHandler {
		public void warning(SAXParseException e) throws SAXException {
	         System.out.println("[ATENCAO]:"); 
	         printInfo(e);
	      }
	      public void error(SAXParseException e) throws SAXException {
	         System.out.println("[ERRO] O arquivo XML nao e valido: "); 
	         printInfo(e);
	      }
	      public void fatalError(SAXParseException e) throws SAXException {
	         System.out.println("[ERRO] Na validacao do arquivo XML:"); 
	         printInfo(e);
	      }
	      private void printInfo(SAXParseException e) {
	      	 System.out.println("   Public ID: "+e.getPublicId());
	      	 System.out.println("   System ID: "+e.getSystemId());
	      	 System.out.println("   Line number: "+e.getLineNumber());
	      	 System.out.println("   Column number: "+e.getColumnNumber());
	      	 System.out.println("   Message: "+e.getMessage());
	      }

	}


}
