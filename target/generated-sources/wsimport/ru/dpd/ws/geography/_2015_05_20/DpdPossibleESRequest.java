
package ru.dpd.ws.geography._2015_05_20;

import javax.xml.datatype.XMLGregorianCalendar;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for dpdPossibleESRequest complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="dpdPossibleESRequest">
 *   <complexContent>
 *     <extension base="{http://dpd.ru/ws/geography/2015-05-20}headerParams">
 *       <sequence>
 *         <element name="auth" type="{http://dpd.ru/ws/geography/2015-05-20}auth"/>
 *         <element name="pickup" type="{http://dpd.ru/ws/geography/2015-05-20}dpdPossibleESPickupDelivery" minOccurs="0"/>
 *         <element name="delivery" type="{http://dpd.ru/ws/geography/2015-05-20}dpdPossibleESPickupDelivery" minOccurs="0"/>
 *         <element name="selfPickup" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         <element name="selfDelivery" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         <element name="serviceCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="pickupDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         <element name="options" type="{http://dpd.ru/ws/geography/2015-05-20}dpdPossibleESOption" minOccurs="0"/>
 *       </sequence>
 *     </extension>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dpdPossibleESRequest", propOrder = {
    "auth",
    "pickup",
    "delivery",
    "selfPickup",
    "selfDelivery",
    "serviceCode",
    "pickupDate",
    "options"
})
public class DpdPossibleESRequest
    extends HeaderParams
{

    @XmlElement(required = true)
    protected Auth auth;
    protected DpdPossibleESPickupDelivery pickup;
    protected DpdPossibleESPickupDelivery delivery;
    protected boolean selfPickup;
    protected boolean selfDelivery;
    @XmlElement(required = true)
    protected String serviceCode;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar pickupDate;
    protected DpdPossibleESOption options;

    /**
     * Gets the value of the auth property.
     * 
     * @return
     *     possible object is
     *     {@link Auth }
     *     
     */
    public Auth getAuth() {
        return auth;
    }

    /**
     * Sets the value of the auth property.
     * 
     * @param value
     *     allowed object is
     *     {@link Auth }
     *     
     */
    public void setAuth(Auth value) {
        this.auth = value;
    }

    /**
     * Gets the value of the pickup property.
     * 
     * @return
     *     possible object is
     *     {@link DpdPossibleESPickupDelivery }
     *     
     */
    public DpdPossibleESPickupDelivery getPickup() {
        return pickup;
    }

    /**
     * Sets the value of the pickup property.
     * 
     * @param value
     *     allowed object is
     *     {@link DpdPossibleESPickupDelivery }
     *     
     */
    public void setPickup(DpdPossibleESPickupDelivery value) {
        this.pickup = value;
    }

    /**
     * Gets the value of the delivery property.
     * 
     * @return
     *     possible object is
     *     {@link DpdPossibleESPickupDelivery }
     *     
     */
    public DpdPossibleESPickupDelivery getDelivery() {
        return delivery;
    }

    /**
     * Sets the value of the delivery property.
     * 
     * @param value
     *     allowed object is
     *     {@link DpdPossibleESPickupDelivery }
     *     
     */
    public void setDelivery(DpdPossibleESPickupDelivery value) {
        this.delivery = value;
    }

    /**
     * Gets the value of the selfPickup property.
     * 
     */
    public boolean isSelfPickup() {
        return selfPickup;
    }

    /**
     * Sets the value of the selfPickup property.
     * 
     */
    public void setSelfPickup(boolean value) {
        this.selfPickup = value;
    }

    /**
     * Gets the value of the selfDelivery property.
     * 
     */
    public boolean isSelfDelivery() {
        return selfDelivery;
    }

    /**
     * Sets the value of the selfDelivery property.
     * 
     */
    public void setSelfDelivery(boolean value) {
        this.selfDelivery = value;
    }

    /**
     * Gets the value of the serviceCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceCode() {
        return serviceCode;
    }

    /**
     * Sets the value of the serviceCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceCode(String value) {
        this.serviceCode = value;
    }

    /**
     * Gets the value of the pickupDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getPickupDate() {
        return pickupDate;
    }

    /**
     * Sets the value of the pickupDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setPickupDate(XMLGregorianCalendar value) {
        this.pickupDate = value;
    }

    /**
     * Gets the value of the options property.
     * 
     * @return
     *     possible object is
     *     {@link DpdPossibleESOption }
     *     
     */
    public DpdPossibleESOption getOptions() {
        return options;
    }

    /**
     * Sets the value of the options property.
     * 
     * @param value
     *     allowed object is
     *     {@link DpdPossibleESOption }
     *     
     */
    public void setOptions(DpdPossibleESOption value) {
        this.options = value;
    }

}
