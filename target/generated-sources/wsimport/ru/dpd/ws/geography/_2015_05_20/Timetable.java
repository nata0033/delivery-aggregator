
package ru.dpd.ws.geography._2015_05_20;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for timetable complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="timetable">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="weekDays" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="workTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "timetable", propOrder = {
    "weekDays",
    "workTime"
})
public class Timetable {

    protected String weekDays;
    protected String workTime;

    /**
     * Gets the value of the weekDays property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWeekDays() {
        return weekDays;
    }

    /**
     * Sets the value of the weekDays property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWeekDays(String value) {
        this.weekDays = value;
    }

    /**
     * Gets the value of the workTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWorkTime() {
        return workTime;
    }

    /**
     * Sets the value of the workTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWorkTime(String value) {
        this.workTime = value;
    }

}
