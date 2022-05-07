package com.jrivera.reporteador.servicio.impl;

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
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.misc.IOUtils;

import java.io.*;
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
        String nombreArchivo = carpetaTemporal + "/" + idIncidencia + "-" + ahora.getTime();

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(nombreArchivo + ".html"));
            bw.write("<html><body>");
            String titulo = "<h1>Reporte de incidencia " + idIncidencia + "</h1>";
            bw.write(titulo);
            bw.newLine();
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
            bw.write(encabezado);
            bw.newLine();

            for (Respuesta respuesta : respuestas) {
                bw.write("<hr/>");
                Usuario usuario = usuarioRepositorio.findUsuarioByIdUsuario(respuesta.getIdUsuario());
                String[] ar = respuesta.getDescripcion().split("#S:");

                String descripcion =
                        (ar.length == 1 ? "" : ("<p>Cambió el estado a <b>" + obtenEstado(Integer.parseInt(ar[1])) + "</b>.</p>\n"))
                                + ar[0];
                String respuesta_ = String.format(Textos.RESPUESTA,
                        respuesta.getActualizacion().toString(),
                        usuario.getNombre() + " " + usuario.getApellido(),
                        descripcion);
                bw.write(respuesta_);
                bw.newLine();
            }
            bw.write("</body></html>");
            bw.close();
            LOG.info("HTMl escrito!");
            try (OutputStream os = new FileOutputStream(nombreArchivo + ".pdf")) {
                PdfRendererBuilder builder = new PdfRendererBuilder();
                builder.useFastMode();
                builder.withUri(new File(nombreArchivo + ".html").toURI().toString());
                builder.toStream(os);
                builder.run();
            }

            return obtenReporte(nombreArchivo + ".pdf");
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return null;
    }
}
