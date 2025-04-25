
package ru.dpd.ws.geography._2015_05_20;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for parcelShop complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="parcelShop">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="parcelShopType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="state" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="address" type="{http://dpd.ru/ws/geography/2015-05-20}address" minOccurs="0"/>
 *         <element name="brand" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="metro" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="clientDepartmentNum" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="geoCoordinates" type="{http://dpd.ru/ws/geography/2015-05-20}geoCoordinates" minOccurs="0"/>
 *         <element name="limits" type="{http://dpd.ru/ws/geography/2015-05-20}limits" minOccurs="0"/>
 *         <element name="schedule" type="{http://dpd.ru/ws/geography/2015-05-20}schedule" maxOccurs="unbounded" minOccurs="0"/>
 *         <element name="extraService" type="{http://dpd.ru/ws/geography/2015-05-20}extraService" maxOccurs="unbounded" minOccurs="0"/>
 *         <element name="services" minOccurs="0">
 *           <complexType>
 *             <complexContent>
 *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 <sequence>
 *                   <element name="serviceCode" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                 </sequence>
 *               </restriction>
 *             </complexContent>
 *           </complexType>
 *         </element>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "parcelShop", propOrder = {
    "code",
    "parcelShopType",
    "state",
    "address",
    "brand",
    "metro",
    "clientDepartmentNum",
    "geoCoordinates",
    "limits",
    "schedule",
    "extraService",
    "services"
})
public class ParcelShop {

    protected String code;
    protected String parcelShopType;
    protected String state;
    protected Address address;
    protected String brand;
    protected String metro;
    protected String clientDepartmentNum;
    protected GeoCoordinates geoCoordinates;
    protected Limits limits;
    protected List<Schedule> schedule;
    @XmlElement(nillable = true)
    protected List<ExtraService> extraService;
    protected ParcelShop.Services services;

    /**
     * Gets the value of the code property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCode(String value) {
        this.code = value;
    }

    /**
     * Gets the value of the parcelShopType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParcelShopType() {
        return parcelShopType;
    }

    /**
     * Sets the value of the parcelShopType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParcelShopType(String value) {
        this.parcelShopType = value;
    }

    /**
     * Gets the value of the state property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the value of the state property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setState(String value) {
        this.state = value;
    }

    /**
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link Address }
     *     
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link Address }
     *     
     */
    public void setAddress(Address value) {
        this.address = value;
    }

    /**
     * Gets the value of the brand property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Sets the value of the brand property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBrand(String value) {
        this.brand = value;
    }

    /**
     * Gets the value of the metro property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMetro() {
        return metro;
    }

    /**
     * Sets the value of the metro property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMetro(String value) {
        this.metro = value;
    }

    /**
     * Gets the value of the clientDepartmentNum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClientDepartmentNum() {
        return clientDepartmentNum;
    }

    /**
     * Sets the value of the clientDepartmentNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClientDepartmentNum(String value) {
        this.clientDepartmentNum = value;
    }

    /**
     * Gets the value of the geoCoordinates property.
     * 
     * @return
     *     possible object is
     *     {@link GeoCoordinates }
     *     
     */
    public GeoCoordinates getGeoCoordinates() {
        return geoCoordinates;
    }

    /**
     * Sets the value of the geoCoordinates property.
     * 
     * @param value
     *     allowed object is
     *     {@link GeoCoordinates }
     *     
     */
    public void setGeoCoordinates(GeoCoordinates value) {
        this.geoCoordinates = value;
    }

    /**
     * Gets the value of the limits property.
     * 
     * @return
     *     possible object is
     *     {@link Limits }
     *     
     */
    public Limits getLimits() {
        return limits;
    }

    /**
     * Sets the value of the limits property.
     * 
     * @param value
     *     allowed object is
     *     {@link Limits }
     *     
     */
    public void setLimits(Limits value) {
        this.limits = value;
    }

    /**
     * Gets the value of the schedule property.
     * 
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the schedule property.</p>
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getSchedule().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Schedule }
     * </p>
     * 
     * 
     * @return
     *     The value of the schedule property.
     */
    public List<Schedule> getSchedule() {
        if (schedule == null) {
            schedule = new ArrayList<>();
        }
        return this.schedule;
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
     * {@link ExtraService }
     * </p>
     * 
     * 
     * @return
     *     The value of the extraService property.
     */
    public List<ExtraService> getExtraService() {
        if (extraService == null) {
            extraService = new ArrayList<>();
        }
        return this.extraService;
    }

    /**
     * Gets the value of the services property.
     * 
     * @return
     *     possible object is
     *     {@link ParcelShop.Services }
     *     
     */
    public ParcelShop.Services getServices() {
        return services;
    }

    /**
     * Sets the value of the services property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParcelShop.Services }
     *     
     */
    public void setServices(ParcelShop.Services value) {
        this.services = value;
    }


    /**
     * <p>Java class for anonymous complex type</p>.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.</p>
     * 
     * <pre>{@code
     * <complexType>
     *   <complexContent>
     *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       <sequence>
     *         <element name="serviceCode" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     *       </sequence>
     *     </restriction>
     *   </complexContent>
     * </complexType>
     * }</pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "serviceCode"
    })
    public static class Services {

        @XmlElement(nillable = true)
        protected List<String> serviceCode;

        /**
         * Gets the value of the serviceCode property.
         * 
         * <p>This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the serviceCode property.</p>
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * </p>
         * <pre>
         * getServiceCode().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * </p>
         * 
         * 
         * @return
         *     The value of the serviceCode property.
         */
        public List<String> getServiceCode() {
            if (serviceCode == null) {
                serviceCode = new ArrayList<>();
            }
            return this.serviceCode;
        }

    }

}
