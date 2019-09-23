package com.swisherdominicana.molde.service

import net.sf.jasperreports.engine.*
import net.sf.jasperreports.engine.export.JRPdfExporter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service

@Service
class ReporteService {

    @Autowired
    SqlService sqlService

    //Logger log = LogManager.getLogger(ReporteService.class)
    private JasperPrint jasperPrint
    private JasperReport jasperReport
    private JRPdfExporter exporter


    String getRecepcionLabelAsB64(String f_lote) {
        try {
            Map<String, Object> params1 = [
                    "f_lote"         : "RC0001",
                    "f_item"         : "RC0001-01",
                    "f_factura"      : "FACT55422",
                    "f_descripcion"  : "producto",
                    "f_net_weight"   : 26.64,
                    "f_cantidad"     : 52.62,
                    "f_lote_suplidor": "SP5522",
                    "f_suplidor"     : "un suplidor cualquiera",
                    "f_grade"        : "DD2324",
                    "f_color"        : "OSCURO",
                    "f_leaf_size"    : 12.5,
                    "f_type"         : "Robusto"
            ]

            InputStream reportInputStream = new ClassPathResource("recepcionlabelv2.jrxml").getInputStream()
            jasperReport = JasperCompileManager.compileReport(reportInputStream)
            jasperPrint = JasperFillManager.fillReport(jasperReport, this.getLabelRecepcionParams(f_lote), new JREmptyDataSource())
            //println JasperExportManager.exportReportToPdf(jasperPrint).encodeBase64()
            return JasperExportManager.exportReportToPdf(jasperPrint).encodeBase64()
        } catch (Exception e) {
            e.printStackTrace()
        }
        return ""
    }

    String getCanastaLabelAsB64(String f_lote) {
        try {
            Map<String, Object> params1 = [
                    "f_lote"         : f_lote
            ]
            InputStream reportInputStream = new ClassPathResource("canastalabel1.jrxml").getInputStream()
            jasperReport = JasperCompileManager.compileReport(reportInputStream)
            jasperPrint = JasperFillManager.fillReport(jasperReport, params1, sqlService.getDataSource().getConnection())
            return JasperExportManager.exportReportToPdf(jasperPrint).encodeBase64()
        } catch (Exception e) {
            e.printStackTrace()
        }
        return ""
    }

    String getBobinaLabelAsB64(List f_lotes) {
        try {
            Map<String, Object> params1 = [
                    "f_lote"         : f_lotes
            ]
            InputStream reportInputStream = new ClassPathResource("labelbobina.jrxml").getInputStream()
            jasperReport = JasperCompileManager.compileReport(reportInputStream)
            jasperPrint = JasperFillManager.fillReport(jasperReport, params1, sqlService.getDataSource().getConnection())
            return JasperExportManager.exportReportToPdf(jasperPrint).encodeBase64()
        } catch (Exception e) {
            e.printStackTrace()
        }
        return ""
    }

    Map getLabelRecepcionParams(String f_lote) {
        String query = """
            SELECT 
              d.f_cantidad,
              d.f_referencia as f_item,
              d.f_produccion,
              dt.f_conduce as f_factura,
              d.f_po,
              d.f_contenedor,
              d.f_lote,
              d.f_gross_weigth,
              d.f_net_weigth as f_net_weight,
              p.f_descripcion,
              s.f_nombre as f_suplidor,
              dt.f_carton_no as f_lote_suplidor,
              dt.f_grade_mark as f_grade,
              dt.f_tamano_hoja as f_leaf_size,
              tt.f_descripcion as f_type,
              ct.f_descripcion as f_color
            FROM 
              public.t_detalle_entrada d 
              INNER JOIN t_productos_sucursal p ON d.f_referencia=p.f_referencia
              INNER JOIN t_orden_compra o ON d.f_orden=o.f_documento
              INNER JOIN t_suplidores s ON o.f_suplidor=s.f_id
              INNER JOIN t_detalle_lote_orden_compra dt on dt.f_lote=d.f_lote
              INNER JOIN t_tipo_tabaco tt on tt.f_id=dt.f_id_tipo_tabaco
              INNER JOIN t_color_tabaco ct on ct.f_id=dt.f_id_color_tabaco
            WHERE
            \td.f_lote= '${f_lote}'
        """
        return  sqlService.executeQueryAsMap(query)
    }

}
