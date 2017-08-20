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
package org.japo.java.models;

import java.io.Serializable;
import org.japo.java.libraries.UtilesValidacion;

/**
 *
 * @author José A. Pacheco Ondoño - joanpaon@gmail.com
 */
public class Model implements Serializable {

    // Número de items
    public static final int NUM_ITEMS = 4;

    // Constantes de acceso
    public static final int POS_LINK = 0;
    public static final int POS_USER = 1;
    public static final int POS_PASS = 2;
    public static final int POS_CODE = 3;

    // Expresiones regulares
    public static final String ER_LINK = "^(https?://)?(([\\w!~*'().&=+$%-]+: )?[\\w!~*'().&=+$%-]+@)?(([0-9]{1,3}\\.){3}[0-9]{1,3}|([\\w!~*'()-]+\\.)*([\\w^-][\\w-]{0,61})?[\\w]\\.[a-z]{2,6})(:[0-9]{1,4})?((/*)|(/+[\\w!~*'().;?:@&=+$,%#-]+)+/*)$";
    public static final String ER_USER = "[\\w!~*'().&=+$%-|@# ]{8,20}";
    public static final String ER_PASS = "[\\w!~*'().&=+$%-|@# ]{8,20}";
    public static final String ER_CODE = "[\\w!~*'().&=+$%-|@# ]{0,20}";

    // Valores por defecto
    public static final String DEF_LINK = "http://user.domain.com";
    public static final String DEF_USER = "Invitado";
    public static final String DEF_PASS = "12345678";
    public static final String DEF_CODE = "PROMO";

    // Campos de la entidad
    private String link;
    private String user;
    private String pass;
    private String code;

    // Constructor Predeterminado
    public Model() {
        link = DEF_LINK;
        user = DEF_USER;
        pass = DEF_PASS;
        code = DEF_CODE;
    }

    // Constructor Parametrizado
    public Model(String link, String user, String pass, String code) {
        // Enlace Página Web
        if (UtilesValidacion.validarDato(link, ER_LINK)) {
            this.link = link;
        } else {
            this.link = DEF_LINK;
        }

        // Usuario
        if (UtilesValidacion.validarDato(user, ER_USER)) {
            this.user = user;
        } else {
            this.user = DEF_USER;
        }

        // Contraseña
        if (UtilesValidacion.validarDato(pass, ER_PASS)) {
            this.pass = pass;
        } else {
            this.pass = DEF_PASS;
        }

        // Código Promoción
        if (UtilesValidacion.validarDato(code, ER_CODE)) {
            this.code = code;
        } else {
            this.code = DEF_CODE;
        }
    }

    // --- INICIO SETTERS / GETTERS
    //
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        if (UtilesValidacion.validarDato(link, ER_LINK)) {
            this.link = link;
        }
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        if (UtilesValidacion.validarDato(user, ER_USER)) {
            this.user = user;
        }
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        if (UtilesValidacion.validarDato(pass, ER_PASS)) {
            this.pass = pass;
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        if (UtilesValidacion.validarDato(code, ER_CODE)) {
            this.code = code;
        }
    }

    // --- FIN SETTERS / GETTERS
}
