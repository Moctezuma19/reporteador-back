package com.jrivera.reporteador.servicio.impl;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.jrivera.reporteador.modelo.Asignacion;
import com.jrivera.reporteador.modelo.Incidencia;
import com.jrivera.reporteador.modelo.Respuesta;
import com.jrivera.reporteador.modelo.Usuario;
import com.jrivera.reporteador.repositorio.AsignacionRepositorio;
import com.jrivera.reporteador.repositorio.IncidenciaRepositorio;
import com.jrivera.reporteador.repositorio.RespuestaRepositorio;
import com.jrivera.reporteador.repositorio.UsuarioRepositorio;
import com.jrivera.reporteador.servicio.ReporteServicio;
import com.jrivera.reporteador.util.Textos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.misc.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.List;

@Service
public class ReporteServicioImpl implements ReporteServicio {
    static final Logger LOG = LoggerFactory.getLogger(ReporteServicioImpl.class);
    @Autowired
    IncidenciaRepositorio incidenciaRepositorio;
    @Autowired
    RespuestaRepositorio respuestaRepositorio;
    @Autowired
    AsignacionRepositorio asignacionRepositorio;
    @Value("${reporteador.temp}")
    private String carpetaTemporal;
    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    private String obtenEstado(int estado) {
        switch (estado) {
            case 0:
                return "Abierta";
            case 1:
                return "En proceso";
            case 2:
                return "Cerrada";
            case 3:
                return "Pendiente por el usuario";
            case 4:
                return "Pendiente por el proveedor";
            case 5:
                return "Pendiente";
            default:
                return "Desconocido";
        }
    }

    private byte[] obtenReporte(String nombre) {
        try {
            File f = new File(nombre);
            if (!f.exists()) {
                return null;
            }
            LOG.info("Obteniendo el reporte en " + nombre);
            InputStream in = new FileInputStream(f);
            return Base64.getEncoder().encode(IOUtils.readFully(in, -1, true));
        } catch (IOException ie) {
            return null;
        }
    }

    @Override
    public byte[] creaReporte(Integer idIncidencia) {
        Incidencia incidencia = incidenciaRepositorio.findIncidenciaByIdIncidencia(idIncidencia);
        if (incidencia == null) {
            return null;
        }
        Asignacion asignacion = asignacionRepositorio.findByIdIncidencia(idIncidencia);

        List<Respuesta> respuestas = respuestaRepositorio.findAllByIdIncidencia(idIncidencia);

        Timestamp ahora = new Timestamp(System.currentTimeMillis());
        String nombreArchivo = carpetaTemporal + "/" + idIncidencia + "-" + ahora.getTime() + ".pdf";

        try {
            PdfWriter escritorPDF = new PdfWriter(nombreArchivo);
            PdfDocument pdfDoc = new PdfDocument(escritorPDF);
            pdfDoc.addNewPage();
            Document documento = new Document(pdfDoc);
            Text texto = new Text("Reporte de incidencia " + idIncidencia);
            texto.setBold();
            Paragraph titulo = new Paragraph(texto);
            documento.add(titulo);
            String estado = obtenEstado(incidencia.getEstado());
            String encabezado = String.format(Textos.ENCABEZADO,
                    incidencia.getTitulo(),
                    incidencia.getCreacion().toString(),
                    incidencia.getActualizacion().toString(),
                    incidencia.getUsuario().getNombre() + " " + incidencia.getUsuario().getApellido(),
                    asignacion != null ?
                            asignacion.getUsuario().getNombre() + " " + asignacion.getUsuario().getApellido() :
                            "Sin ingeniero de servicio asignado",
                    estado,
                    incidencia.getDescripcion());
            Paragraph incidenciaEncabezado = new Paragraph(encabezado);
            documento.add(incidenciaEncabezado);
            SolidLine solidLine = new SolidLine(1f);

            LineSeparator separadorDeLinea = new LineSeparator(solidLine);
            for (Respuesta respuesta : respuestas) {
                documento.add(separadorDeLinea);
                Usuario usuario = usuarioRepositorio.findUsuarioByIdUsuario(respuesta.getIdUsuario());
                String[] ar = respuesta.getDescripcion().split("#S:");

                String descripcion =
                        (ar.length == 1 ? "" : ("Cambió el estado a \"" + obtenEstado(Integer.parseInt(ar[1])) + "\"\n"))
                                + ar[0];
                String respuesta_ = String.format(Textos.RESPUESTA,
                        respuesta.getActualizacion().toString(),
                        usuario.getNombre() + " " + usuario.getApellido(),
                        descripcion);
                Paragraph incidenciaRespuesta = new Paragraph(respuesta_);
                documento.add(incidenciaRespuesta);
            }
            // Closing the document
            documento.close();
            return obtenReporte(nombreArchivo);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return null;
    }
}
