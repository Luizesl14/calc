package com.tech.controller;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Controller
public class SVGColorController {

    @PostMapping("/uploadSVG")
    public String handleSVGUpload(@RequestParam("file") MultipartFile file, Model model) {
        if (!file.isEmpty()) {
            try {
                // Converte o arquivo MultipartFile para File
                File svgFile = convertMultipartFileToFile(file);

                // Cria uma instância da fábrica de documentos SVG
                String parser = XMLResourceDescriptor.getXMLParserClassName();
                SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
                Document svgDocument = f.createDocument(svgFile.toURI().toString());

                // Processa o documento SVG
                processSVGDocument(svgDocument, model);

                // Retorna a página de resultado
                return "result";
            } catch (Exception e) {
                e.printStackTrace();
                // Lida com o erro
            }
        }
        return "redirect:/uploadError";
    }

    private void processSVGDocument(Document svgDocument, Model model) {
        // Obtemos a raiz do documento SVG
        Node root = svgDocument.getDocumentElement();

        // Processa os elementos filhos
        processSVGElements(root.getChildNodes(), model);
    }

    private void processSVGElements(NodeList nodeList, Model model) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            // Exemplo: Extrair informações de retângulos
            if ("rect".equals(node.getNodeName())) {
                String fill = node.getAttributes().getNamedItem("fill").getNodeValue();

                // Se a cor é ciano (exemplo)
                if (isCyanColor(fill)) {
                    // Calcular a área do retângulo
                    String width = node.getAttributes().getNamedItem("width").getNodeValue();
                    String height = node.getAttributes().getNamedItem("height").getNodeValue();
                    float area = Float.parseFloat(width) * Float.parseFloat(height);

                    model.addAttribute("cyanArea", area);
                }
            }

            // Chama recursivamente para processar os filhos
            processSVGElements(node.getChildNodes(), model);
        }
    }

    private boolean isCyanColor(String fill) {
        fill = fill.toLowerCase();
        return fill.equals("cyan");
    }

    // Função para converter MultipartFile em File (depende da biblioteca usada)
    private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File tempFile = File.createTempFile("temp", null);
        try {
            Files.copy(multipartFile.getInputStream(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Falha ao salvar o arquivo temporário", e);
        }
        return tempFile;
    }
}
