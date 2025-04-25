
package ru.dpd.ws.geography._2015_05_20;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for extraService complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="extraService">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="esCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="params" type="{http://dpd.ru/ws/geography/2015-05-20}extraServiceParam" maxOccurs="unbounded" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "extraService", propOrder = {
    "esCode",
    "params"
})
public class ExtraService {

    protected String esCode;
    protected List<ExtraServiceParam> params;

    /**
     * Gets the value of the esCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEsCode() {
        return esCode;
    }

    /**
     * Sets the value of the esCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEsCode(String value) {
        this.esCode = value;
    }

    /**
     * Gets the value of the params property.
     * 
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the params property.</p>
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getParams().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ExtraServiceParam }
     * </p>
     * 
     * 
     * @return
     *     The value of the params property.
     */
    public List<ExtraServiceParam> getParams() {
        if (params == null) {
            params = new ArrayList<>();
        }
        return this.params;
    }

}
