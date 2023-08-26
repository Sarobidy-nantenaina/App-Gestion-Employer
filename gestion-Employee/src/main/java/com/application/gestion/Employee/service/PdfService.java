package com.application.gestion.Employee.service;

import com.application.gestion.Employee.model.Employee;
import com.application.gestion.Employee.model.Entreprise;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Service
public class PdfService {

  private final TemplateEngine templateEngine;

  public PdfService(TemplateEngine templateEngine) {
    this.templateEngine = templateEngine;
  }

  public byte[] generatePdf(Employee employee, List<Entreprise> entreprises) throws Exception {
    Context context = new Context();
    context.setVariable("employee", employee);
    context.setVariable("entreprises", entreprises);

    String htmlContent = templateEngine.process("version_pdf", context);

    OutputStream outputStream = new ByteArrayOutputStream();
    ITextRenderer renderer = new ITextRenderer();
    renderer.setDocumentFromString(htmlContent);
    renderer.layout();
    renderer.createPDF(outputStream);

    return ((ByteArrayOutputStream) outputStream).toByteArray();
  }
}
