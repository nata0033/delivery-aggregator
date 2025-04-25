
package ru.dpd.ws.calculator._2012_03_20;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for serviceCostInternationalWithExtraServicesRequest complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="serviceCostInternationalWithExtraServicesRequest">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="auth" type="{http://dpd.ru/ws/calculator/2012-03-20}auth"/>
 *         <element name="pickup" type="{http://dpd.ru/ws/calculator/2012-03-20}cityInternationalRequest"/>
 *         <element name="delivery" type="{http://dpd.ru/ws/calculator/2012-03-20}cityInternationalRequest"/>
 *         <element name="selfPickup" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         <element name="selfDelivery" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         <element name="weight" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         <element name="length" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         <element name="width" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         <element name="height" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         <element name="declaredValue" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         <element name="insurance" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         <element name="extraService" type="{http://dpd.ru/ws/calculator/2012-03-20}extraServiceWithParams" maxOccurs="unbounded" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "serviceCostInternationalWithExtraServicesRequest", propOrder = {
    "auth",
    "pickup",
    "delivery",
    "selfPickup",
    "selfDelivery",
    "weight",
    "length",
    "width",
    "height",
    "declaredValue",
    "insurance",
    "extraService"
})
public class ServiceCostInternationalWithExtraServicesRequest {

    @XmlElement(required = true)
    protected Auth auth;
    @XmlElement(required = true)
    protected CityInternationalRequest pickup;
    @XmlElement(required = true)
    protected CityInternationalRequest delivery;
    protected boolean selfPickup;
    protected boolean selfDelivery;
    protected double weight;
    protected long length;
    protected long width;
    protected long height;
    protected Double declaredValue;
    protected Boolean insurance;
    @XmlElement(nillable = true)
    protected List<ExtraServiceWithParams> extraService;

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
     *     {@link CityInternationalRequest }
     *     
     */
    public CityInternationalRequest getPickup() {
        return pickup;
    }

    /**
     * Sets the value of the pickup property.
     * 
     * @param value
     *     allowed object is
     *     {@link CityInternationalRequest }
     *     
     */
    public void setPickup(CityInternationalRequest value) {
        this.pickup = value;
    }

    /**
     * Gets the value of the delivery property.
     * 
     * @return
     *     possible object is
     *     {@link CityInternationalRequest }
     *     
     */
    public CityInternationalRequest getDelivery() {
        return delivery;
    }

    /**
     * Sets the value of the delivery property.
     * 
     * @param value
     *     allowed object is
     *     {@link CityInternationalRequest }
     *     
     */
    public void setDelivery(CityInternationalRequest value) {
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
     * Gets the value of the length property.
     * 
     */
    public long getLength() {
        return length;
    }

    /**
     * Sets the value of the length property.
     * 
     */
    public void setLength(long value) {
        this.length = value;
    }

    /**
     * Gets the value of the width property.
     * 
     */
    public long getWidth() {
        return width;
    }

    /**
     * Sets the value of the width property.
     * 
     */
    public void setWidth(long value) {
        this.width = value;
    }

    /**
     * Gets the value of the height property.
     * 
     */
    public long getHeight() {
        return height;
    }

    /**
     * Sets the value of the height property.
     * 
     */
    public void setHeight(long value) {
        this.height = value;
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

    /**
     * Gets the value of the insurance property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isInsurance() {
        return insurance;
    }

    /**
     * Sets the value of the insurance property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setInsurance(Boolean value) {
        this.insurance = value;
    }

    /**
     * Gets the value of the extraService property.
     * 
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the extraService property.</p>
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getExtraService().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ExtraServiceWithParams }
     * </p>
     * 
     * 
     * @return
     *     The value of the extraService property.
     */
    public List<ExtraServiceWithParams> getExtraService() {
        if (extraService == null) {
            extraService = new ArrayList<>();
        }
        return this.extraService;
    }

}
