/*
 * Copyright 2017 José A. Pacheco Ondoño - joanpaon@gmail.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.japo.java.controllers;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import javax.swing.JOptionPane;
import org.japo.java.models.Model;
import org.japo.java.views.View;
import org.japo.java.libraries.UtilesApp;
import org.japo.java.libraries.UtilesSwing;
import org.japo.java.interfaces.IDataAccessController;
import org.japo.java.libraries.UtilesValidacion;

/**
 *
 * @author José A. Pacheco Ondoño - joanpaon@gmail.com
 */
public class Controller {

    // Fichero Propiedades
    public static final String FICHERO = "app.properties";

    // Referencias
    private final Model model;
    private final View view;
    private final Properties prpApp;
    private final IDataAccessController dac;

    // Constructor Parametrizado
    public Controller(Model model, View view) {
        // Memorizar Referencias
        this.model = model;
        this.view = view;

        // Cargar Propiedades Aplicación
        this.prpApp = UtilesApp.cargarPropiedades(FICHERO);

        // *** Controlador de Persistencia ***
        this.dac = new DataAccessControllerSBIN();
    }

    // Persistencia > Modelo > Vista
    public void procesarImportacion(ActionEvent evt) {
        try {
            // Fichero de Datos
            String fichero = prpApp.getProperty("fichero_datos");

            // Persistencia > Modelo
            dac.importarModelo(model, fichero);

            // Modelo > Vista
            sincronizarModeloVista(model, view);

            // Validar Datos Importados > Vista
            validarControlesSubjetivos(view);

            // Mensaje - Importación OK
            String msg = "Datos importados correctamente";
            JOptionPane.showMessageDialog(view, msg);
        } catch (Exception e) {
            // Mensaje - Importación NO
            String msg = "Error al importar los datos";
            JOptionPane.showMessageDialog(view, msg);
        }
    }

    // Vista > Modelo > Persistencia
    public void procesarExportacion(ActionEvent evt) {
        // Validar Datos Vista
        if (validarControlesSubjetivos(view)) {
            try {
                // Vista > Modelo
                sincronizarVistaModelo(view, model);

                // Fichero de Datos
                String fichero = prpApp.getProperty("fichero_datos");

                // Modelo > Persistencia
                dac.exportarModelo(model, fichero);

                // Mensaje - Exportación OK
                String msg = "Datos exportados correctamente";
                JOptionPane.showMessageDialog(view, msg);
            } catch (Exception e) {
                // Mensaje - Exportación NO
                String msg = "Error al exportar los datos";
                JOptionPane.showMessageDialog(view, msg);
            }
        } else {
            // Mensaje - Validación Pendiente
            JOptionPane.showMessageDialog(view, "Hay datos erróneos.");
        }
    }

    // Modelo > Vista 
    public void sincronizarModeloVista(Model model, View view) {
        view.txfLink.setText(model.getLink());
        view.txfUser.setText(model.getUser());
        if (view.cbxPass.isSelected()) {
            view.txfPass.setText(model.getPass());
        }
        view.txfCode.setText(model.getCode());
    }

    // Vista (Subjetivo) > Modelo
    private void sincronizarVistaModelo(View view, Model model) {
        model.setLink(view.txfLink.getText());
        model.setUser(view.txfUser.getText());
        if (view.cbxPass.isSelected()) {
            model.setPass(view.txfPass.getText());
        }
        model.setCode(view.txfCode.getText());
    }

    // Validar Controles Subjetivos
    private boolean validarControlesSubjetivos(View view) {
        // Validación Campos de Texto
        boolean linkOK = UtilesValidacion.validarCampoTexto(view.txfLink, Model.ER_LINK, "?");
        boolean userOK = UtilesValidacion.validarCampoTexto(view.txfUser, Model.ER_USER, "?");
        boolean passOK = view.cbxPass.isSelected()
                ? UtilesValidacion.validarCampoTexto(view.txfPass, Model.ER_PASS, "?") : true;
        boolean codeOK = UtilesValidacion.validarCampoTexto(view.txfCode, Model.ER_CODE, "");

        // Devuelve Validación
        return linkOK && userOK && passOK && codeOK;
    }

    // Propiedades Vista > Estado Vista
    public void restaurarEstadoVista(View view, Properties prp) {
        // Establecer Favicon
        UtilesSwing.establecerFavicon(view, prp.getProperty("ruta_favicon"));

        // Establece Lnf
        UtilesSwing.establecerLnF(prp.getProperty("lnf", UtilesSwing.WINDOWS));

        // Activa Singleton
        if (!UtilesApp.activarInstancia(prp.getProperty("puerto_bloqueo", UtilesApp.PUERTO_BLOQUEO))) {
            UtilesSwing.terminarPrograma(view);
        }

        // Activa otras propiedades
    }

    // Gestión Cierre Vista
    public void procesarCierreVista(WindowEvent evt) {
        // Memorizar Estado de la Applicación
        memorizarEstadoVista(prpApp);

        // Otras Acciones
    }

    // Estado Actual > Persistencia
    public void memorizarEstadoVista(Properties prpApp) {
//        // Actualiza Propiedades Estado Actual
//
//        // Guardar Estado Actual
//        UtilesApp.guardarPropiedades(prpApp);
    }

    // Lanzar Navegador
    public void lanzarNavegador(String url) {
        // Acceso Lanzamiento Aplicaciones Nativas
        Desktop desktop = Desktop.getDesktop();

        // Referencia URI a lanzar
        URI uri = null;

        // URI > Navegador
        try {
            // Analizar URL
            if (url == null || url.trim().length() == 0) {
                uri = new URI("https://www.google.com");
            } else if (UtilesValidacion.validarDato(url, Model.ER_LINK)) {
                uri = new URI(url);
            } else if (UtilesValidacion.validarDato("https://www.google.com/search?q=" + url, Model.ER_LINK)) {
                uri = new URI("https://www.google.com/search?q=" + url);
            }

            // Lanzar URI
            desktop.browse(uri);
        } catch (URISyntaxException | IOException ex) {
            JOptionPane.showMessageDialog(view, "Error en el enlace");
        }
    }

    // Visibilidad Contraseña
    public void procesarPassword(ActionEvent evt) {
        if (view.cbxPass.isSelected()) {
            view.txfPass.setText(model.getPass());
            view.txfPass.setEnabled(true);
        } else if (UtilesValidacion.validarDato(view.txfPass.getText(), Model.ER_PASS)) {
            model.setPass(view.txfPass.getText());
            view.txfPass.setText("•••••••••••••••");
            view.txfPass.setEnabled(false);
        } else {
            view.cbxPass.setSelected(true);
        }
    }
}
