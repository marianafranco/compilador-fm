package compilador.metacompilador;

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

import compilador.metacompilador.estruturas.AFD;
import compilador.metacompilador.estruturas.APE;
import compilador.metacompilador.estruturas.Estado;
import compilador.metacompilador.estruturas.Transicao;


public class MontaMetaAFD {
	
	private String arquivoXML = "metacompilador.xml";
	
	public MontaMetaAFD(){
		
	}
	
	
	public boolean executa(APE automato){
		// Cria o automato a partir do aquivo XML.
		return parseXML(this.arquivoXML, automato);
	}
	
	
	public boolean parseXML(String arquivoXML, APE automato){
		
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
			
			// Pega todos os nterminais
			XPathExpression expr = xpath.compile("//nterminal");
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			
			automato.setNumSubmaquinas(nodes.getLength());
			
			// Adiciona cada nterminais
			for (int i = 0; i < nodes.getLength(); i++) {
				
				// Recupera o nome
				expr = xpath.compile("./nome/text()");
				result = expr.evaluate(nodes.item(i), XPathConstants.NODE);
				Node node =  (Node) result;
				String nome = node.getNodeValue();
				//System.out.println("Nterminal: " + nome);
				
				
				// Cria o AFD correspondente ao não terminal
				AFD nterminal = new AFD(nome);
				
				// Recupera os estados
				expr = xpath.compile("./estado");
				result = expr.evaluate(nodes.item(i), XPathConstants.NODESET);
				NodeList nodesEstado = (NodeList) result;
				
				// Seta o número de estados existentes para inicializar o 
				// vetor de estados
				nterminal.setNumEstados(nodesEstado.getLength());
				
				//System.out.println("NumEstados: " + nodesEstado.getLength());
				
				// Adiciona cada estado no AFD
				for (int j = 0; j < nodesEstado.getLength(); j++) {
					//System.out.println("\tEstado:");
					
					// Recupera o id
					expr = xpath.compile("./id/text()");
					result = expr.evaluate(nodesEstado.item(j), XPathConstants.NODE);
					node =  (Node) result;
					int id = Integer.parseInt(node.getNodeValue()) ;
					//System.out.println("\t\tID: " + id);
					
					// Recupera se é final ou não
					expr = xpath.compile("./final/text()");
					result = expr.evaluate(nodesEstado.item(j), XPathConstants.NODE);
					node =  (Node) result;
					boolean aceitacao = Boolean.parseBoolean(node.getNodeValue());
					//System.out.println("\t\tFINAL: " + aceitacao);
					
					// Cria o estado
					Estado estado = new Estado(id, aceitacao);
					
					// Recupera as transições
					expr = xpath.compile("./transicao");
					result = expr.evaluate(nodesEstado.item(j), XPathConstants.NODESET);
					NodeList nodesTransicao = (NodeList) result;
					
					// Seta o número de transições existente para inicializar o 
					// vetor de transições
					estado.setNumTransicoes(nodesTransicao.getLength());
					
					// Adiciona cada transição ao estado
					for (int k = 0; k < nodesTransicao.getLength(); k++) {
						
						//System.out.println("\t\tTransicao:");
						
						// Recupera as entradas
						expr = xpath.compile("./entradas/text()");
						result = expr.evaluate(nodesTransicao.item(k), XPathConstants.NODE);
						node =  (Node) result;
						String entrada = node.getNodeValue();
						//System.out.println("\t\t\tENTRADA: " + entrada);
						
						// Recupera o id do próximo estado
						expr = xpath.compile("./proximo/text()");
						result = expr.evaluate(nodesTransicao.item(k), XPathConstants.NODE);
						node =  (Node) result;
						int proximo = Integer.parseInt(node.getNodeValue());
						//System.out.println("\t\t\tPROXIMO: " + proximo);
						
						// Cria a transição
						Transicao transicao = new Transicao(proximo, entrada);
						
						// Adiciona a transição ao estado
						estado.adicionaTransicao(transicao, k);
					}
					
					// Adiciona o estado no automato
					nterminal.adicionaEstado(estado, j);
					
				}
				
				// Adiciona o nterminal ao APE
				automato.adicionaSubmaquina(nterminal, i);
			}
			
			System.out.println("[INFO] Automato METACOMPILADOR montado com sucesso.");
			return true;
		
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("[ERRO] Erro ao fazer o parser do arquivo XML");
			return false;
		}
		
	}
	
	private static class MeuErrorHandler implements ErrorHandler {
		public void warning(SAXParseException e) throws SAXException {
	         System.out.println("[ATENCAO]:"); 
	         printInfo(e);
	         throw new SAXException();
	      }
	      public void error(SAXParseException e) throws SAXException {
	         System.out.println("[ERRO] O arquivo XML nao e valido: "); 
	         printInfo(e);
	         throw new SAXException();
	      }
	      public void fatalError(SAXParseException e) throws SAXException {
	         System.out.println("[ERRO] Na validacao do arquivo XML:"); 
	         printInfo(e);
	         throw new SAXException();
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
