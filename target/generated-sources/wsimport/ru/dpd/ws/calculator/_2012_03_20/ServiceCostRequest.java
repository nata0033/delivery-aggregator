
package ru.dpd.ws.calculator._2012_03_20;

import javax.xml.datatype.XMLGregorianCalendar;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for serviceCostRequest complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="serviceCostRequest">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="auth" type="{http://dpd.ru/ws/calculator/2012-03-20}auth"/>
 *         <element name="pickup" type="{http://dpd.ru/ws/calculator/2012-03-20}cityRequest"/>
 *         <element name="delivery" type="{http://dpd.ru/ws/calculator/2012-03-20}cityRequest"/>
 *         <element name="selfPickup" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         <element name="selfDelivery" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         <element name="weight" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         <element name="volume" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         <element name="serviceCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="pickupDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         <element name="maxDays" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         <element name="maxCost" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         <element name="declaredValue" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "serviceCostRequest", propOrder = {
    "auth",
    "pickup",
    "delivery",
    "selfPickup",
    "selfDelivery",
    "weight",
    "volume",
    "serviceCode",
    "pickupDate",
    "maxDays",
    "maxCost",
    "declaredValue"
})
public class ServiceCostRequest {

    @XmlElement(required = true)
    protected Auth auth;
    @XmlElement(required = true)
    protected CityRequest pickup;
    @XmlElement(required = true)
    protected CityRequest delivery;
    protected boolean selfPickup;
    protected boolean selfDelivery;
    protected double weight;
    protected Double volume;
    protected String serviceCode;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar pickupDate;
    protected Integer maxDays;
    protected Double maxCost;
    protected Double declaredValue;

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
     *     {@link CityRequest }
     *     
     */
    public CityRequest getPickup() {
        return pickup;
    }

    /**
     * Sets the value of the pickup property.
     * 
     * @param value
     *     allowed object is
     *     {@link CityRequest }
     *     
     */
    public void setPickup(CityRequest value) {
        this.pickup = value;
    }

    /**
     * Gets the value of the delivery property.
     * 
     * @return
     *     possible object is
     *     {@link CityRequest }
     *     
     */
    public CityRequest getDelivery() {
        return delivery;
    }

    /**
     * Sets the value of the delivery property.
     * 
     * @param value
     *     allowed object is
     *     {@link CityRequest }
     *     
     */
    public void setDelivery(CityRequest value) {
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
     * Gets the value of the weight property.
     * 
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Sets the value of the weight property.
     * 
     */
    public void setWeight(double value) {
        this.weight = value;
    }

    /**
     * Gets the value of the volume property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getVolume() {
        return volume;
    }

    /**
     * Sets the value of the volume property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setVolume(Double value) {
        this.volume = value;
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
     * Gets the value of the maxDays property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMaxDays() {
        return maxDays;
    }

    /**
     * Sets the value of the maxDays property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMaxDays(Integer value) {
        this.maxDays = value;
    }

    /**
     * Gets the value of the maxCost property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getMaxCost() {
        return maxCost;
    }

    /**
     * Sets the value of the maxCost property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setMaxCost(Double value) {
        this.maxCost = value;
    }

    /**
     * Gets the value of the declaredValue property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getDeclaredValue() {
        return declaredValue;
    }

    /**
     * Sets the value of the declaredValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setDeclaredValue(Double value) {
        this.declaredValue = value;
    }

}
