package com.application.gestion.Employee.controler;

import com.application.gestion.Employee.model.Employee;
import com.application.gestion.Employee.model.Entreprise;
import com.application.gestion.Employee.service.EmployeeService;
import com.application.gestion.Employee.service.EntrepriseService;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import com.itextpdf.html2pdf.ConverterProperties;


@Controller
@AllArgsConstructor
public class employeeFIchePdfControler {

  private final EmployeeService employeeService;
  private final EntrepriseService entrepriseService;
  private final TemplateEngine templateEngine;

  @GetMapping("/employees/{id}/generate-pdf")
  public void generatePdf(@PathVariable("id") Long id, HttpServletResponse response, Model model) throws
      IOException,
      DocumentException {
    // Récupérer les données de l'employé et d'autres informations nécessaires
    Employee employee = employeeService.getEmployeeById(id);
    List<Entreprise> entreprises = entrepriseService.getAllEntreprises();

    // Créer un contexte Thymeleaf à partir du modèle Spring
    Context thymeleafContext = new Context();
    thymeleafContext.setVariable("employee", employee);
    thymeleafContext.setVariable("entreprises", entreprises);

    // Récupérer le contenu HTML généré à partir du modèle Thymeleaf
    String htmlContent = templateEngine.process("version_pdf", thymeleafContext);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PdfWriter writer = new PdfWriter(outputStream);
    PdfDocument pdfDocument = new PdfDocument(writer);

    float someExtraWidthInPoints = 20.0f; // Valeur personnalisée en points
    float someExtraHeightInPoints = 20.0f; // Valeur personnalisée en points

    // Définir une largeur personnalisée pour la taille de la page (en points)
    float pageWidth = PageSize.A4.getWidth() + someExtraWidthInPoints;

    // Calculer la hauteur proportionnelle pour conserver les proportions de la page A4
    float pageHeight = pageWidth * PageSize.A4.getHeight() / PageSize.A4.getWidth();

    // Ajouter une hauteur supplémentaire à la page
    pageHeight += someExtraHeightInPoints;

    Document document = new Document(pdfDocument, new PageSize(pageWidth, pageHeight));

    // Convertir le contenu HTML en PDF en utilisant iText's HTMLConverter
    ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
    ConverterProperties converterProperties = new ConverterProperties();
    HtmlConverter.convertToPdf(inputStream, pdfDocument, converterProperties);

    document.close();
    pdfDocument.close();

    // Écrire le PDF généré dans le flux de sortie de la réponse
    response.setContentType("application/pdf");
    response.setHeader("Content-Disposition", "inline; filename=fiche_employe.pdf");
    response.setContentLength(outputStream.size());

    outputStream.writeTo(response.getOutputStream());
    outputStream.close();
  }


}
