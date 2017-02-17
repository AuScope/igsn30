//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.02.16 at 10:36:31 AM AWST 
//


package org.csiro.igsn.jaxb.oai.bindings.igsn;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for geometryType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="geometryType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="LineString"/>
 *     &lt;enumeration value="MultiLineString"/>
 *     &lt;enumeration value="Point"/>
 *     &lt;enumeration value="MultiPoint"/>
 *     &lt;enumeration value="Polygon"/>
 *     &lt;enumeration value="MultiPolygon"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "geometryType")
@XmlEnum
public enum GeometryType {

    @XmlEnumValue("LineString")
    LINE_STRING("LineString"),
    @XmlEnumValue("MultiLineString")
    MULTI_LINE_STRING("MultiLineString"),
    @XmlEnumValue("Point")
    POINT("Point"),
    @XmlEnumValue("MultiPoint")
    MULTI_POINT("MultiPoint"),
    @XmlEnumValue("Polygon")
    POLYGON("Polygon"),
    @XmlEnumValue("MultiPolygon")
    MULTI_POLYGON("MultiPolygon");
    private final String value;

    GeometryType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static GeometryType fromValue(String v) {
        for (GeometryType c: GeometryType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}