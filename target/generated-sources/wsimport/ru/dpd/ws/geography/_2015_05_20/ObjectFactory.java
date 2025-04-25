
package ru.dpd.ws.geography._2015_05_20;

import javax.xml.namespace.QName;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ru.dpd.ws.geography._2015_05_20 package. 
 * <p>An ObjectFactory allows you to programmatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private static final QName _WSFault_QNAME = new QName("http://dpd.ru/ws/geography/2015-05-20", "WSFault");
    private static final QName _GetCitiesCashPay_QNAME = new QName("http://dpd.ru/ws/geography/2015-05-20", "getCitiesCashPay");
    private static final QName _GetCitiesCashPayResponse_QNAME = new QName("http://dpd.ru/ws/geography/2015-05-20", "getCitiesCashPayResponse");
    private static final QName _GetParcelShops_QNAME = new QName("http://dpd.ru/ws/geography/2015-05-20", "getParcelShops");
    private static final QName _GetParcelShopsResponse_QNAME = new QName("http://dpd.ru/ws/geography/2015-05-20", "getParcelShopsResponse");
    private static final QName _GetPossibleExtraService_QNAME = new QName("http://dpd.ru/ws/geography/2015-05-20", "getPossibleExtraService");
    private static final QName _GetPossibleExtraServiceResponse_QNAME = new QName("http://dpd.ru/ws/geography/2015-05-20", "getPossibleExtraServiceResponse");
    private static final QName _GetStoragePeriod_QNAME = new QName("http://dpd.ru/ws/geography/2015-05-20", "getStoragePeriod");
    private static final QName _GetStoragePeriodResponse_QNAME = new QName("http://dpd.ru/ws/geography/2015-05-20", "getStoragePeriodResponse");
    private static final QName _GetTerminalsSelfDelivery2_QNAME = new QName("http://dpd.ru/ws/geography/2015-05-20", "getTerminalsSelfDelivery2");
    private static final QName _GetTerminalsSelfDelivery2Response_QNAME = new QName("http://dpd.ru/ws/geography/2015-05-20", "getTerminalsSelfDelivery2Response");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ru.dpd.ws.geography._2015_05_20
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ParcelShop }
     * 
     * @return
     *     the new instance of {@link ParcelShop }
     */
    public ParcelShop createParcelShop() {
        return new ParcelShop();
    }

    /**
     * Create an instance of {@link TerminalSelf }
     * 
     * @return
     *     the new instance of {@link TerminalSelf }
     */
    public TerminalSelf createTerminalSelf() {
        return new TerminalSelf();
    }

    /**
     * Create an instance of {@link WSFault }
     * 
     * @return
     *     the new instance of {@link WSFault }
     */
    public WSFault createWSFault() {
        return new WSFault();
    }

    /**
     * Create an instance of {@link GetCitiesCashPay }
     * 
     * @return
     *     the new instance of {@link GetCitiesCashPay }
     */
    public GetCitiesCashPay createGetCitiesCashPay() {
        return new GetCitiesCashPay();
    }

    /**
     * Create an instance of {@link GetCitiesCashPayResponse }
     * 
     * @return
     *     the new instance of {@link GetCitiesCashPayResponse }
     */
    public GetCitiesCashPayResponse createGetCitiesCashPayResponse() {
        return new GetCitiesCashPayResponse();
    }

    /**
     * Create an instance of {@link GetParcelShops }
     * 
     * @return
     *     the new instance of {@link GetParcelShops }
     */
    public GetParcelShops createGetParcelShops() {
        return new GetParcelShops();
    }

    /**
     * Create an instance of {@link GetParcelShopsResponse }
     * 
     * @return
     *     the new instance of {@link GetParcelShopsResponse }
     */
    public GetParcelShopsResponse createGetParcelShopsResponse() {
        return new GetParcelShopsResponse();
    }

    /**
     * Create an instance of {@link GetPossibleExtraService }
     * 
     * @return
     *     the new instance of {@link GetPossibleExtraService }
     */
    public GetPossibleExtraService createGetPossibleExtraService() {
        return new GetPossibleExtraService();
    }

    /**
     * Create an instance of {@link GetPossibleExtraServiceResponse }
     * 
     * @return
     *     the new instance of {@link GetPossibleExtraServiceResponse }
     */
    public GetPossibleExtraServiceResponse createGetPossibleExtraServiceResponse() {
        return new GetPossibleExtraServiceResponse();
    }

    /**
     * Create an instance of {@link GetStoragePeriod }
     * 
     * @return
     *     the new instance of {@link GetStoragePeriod }
     */
    public GetStoragePeriod createGetStoragePeriod() {
        return new GetStoragePeriod();
    }

    /**
     * Create an instance of {@link GetStoragePeriodResponse }
     * 
     * @return
     *     the new instance of {@link GetStoragePeriodResponse }
     */
    public GetStoragePeriodResponse createGetStoragePeriodResponse() {
        return new GetStoragePeriodResponse();
    }

    /**
     * Create an instance of {@link GetTerminalsSelfDelivery2 }
     * 
     * @return
     *     the new instance of {@link GetTerminalsSelfDelivery2 }
     */
    public GetTerminalsSelfDelivery2 createGetTerminalsSelfDelivery2() {
        return new GetTerminalsSelfDelivery2();
    }

    /**
     * Create an instance of {@link GetTerminalsSelfDelivery2Response }
     * 
     * @return
     *     the new instance of {@link GetTerminalsSelfDelivery2Response }
     */
    public GetTerminalsSelfDelivery2Response createGetTerminalsSelfDelivery2Response() {
        return new GetTerminalsSelfDelivery2Response();
    }

    /**
     * Create an instance of {@link Auth }
     * 
     * @return
     *     the new instance of {@link Auth }
     */
    public Auth createAuth() {
        return new Auth();
    }

    /**
     * Create an instance of {@link SelfTerminals }
     * 
     * @return
     *     the new instance of {@link SelfTerminals }
     */
    public SelfTerminals createSelfTerminals() {
        return new SelfTerminals();
    }

    /**
     * Create an instance of {@link Address }
     * 
     * @return
     *     the new instance of {@link Address }
     */
    public Address createAddress() {
        return new Address();
    }

    /**
     * Create an instance of {@link GeoCoordinates }
     * 
     * @return
     *     the new instance of {@link GeoCoordinates }
     */
    public GeoCoordinates createGeoCoordinates() {
        return new GeoCoordinates();
    }

    /**
     * Create an instance of {@link Schedule }
     * 
     * @return
     *     the new instance of {@link Schedule }
     */
    public Schedule createSchedule() {
        return new Schedule();
    }

    /**
     * Create an instance of {@link Timetable }
     * 
     * @return
     *     the new instance of {@link Timetable }
     */
    public Timetable createTimetable() {
        return new Timetable();
    }

    /**
     * Create an instance of {@link ExtraService }
     * 
     * @return
     *     the new instance of {@link ExtraService }
     */
    public ExtraService createExtraService() {
        return new ExtraService();
    }

    /**
     * Create an instance of {@link ExtraServiceParam }
     * 
     * @return
     *     the new instance of {@link ExtraServiceParam }
     */
    public ExtraServiceParam createExtraServiceParam() {
        return new ExtraServiceParam();
    }

    /**
     * Create an instance of {@link DpdPossibleESRequest }
     * 
     * @return
     *     the new instance of {@link DpdPossibleESRequest }
     */
    public DpdPossibleESRequest createDpdPossibleESRequest() {
        return new DpdPossibleESRequest();
    }

    /**
     * Create an instance of {@link HeaderParams }
     * 
     * @return
     *     the new instance of {@link HeaderParams }
     */
    public HeaderParams createHeaderParams() {
        return new HeaderParams();
    }

    /**
     * Create an instance of {@link DpdPossibleESPickupDelivery }
     * 
     * @return
     *     the new instance of {@link DpdPossibleESPickupDelivery }
     */
    public DpdPossibleESPickupDelivery createDpdPossibleESPickupDelivery() {
        return new DpdPossibleESPickupDelivery();
    }

    /**
     * Create an instance of {@link DpdPossibleESOption }
     * 
     * @return
     *     the new instance of {@link DpdPossibleESOption }
     */
    public DpdPossibleESOption createDpdPossibleESOption() {
        return new DpdPossibleESOption();
    }

    /**
     * Create an instance of {@link DpdPossibleESResult }
     * 
     * @return
     *     the new instance of {@link DpdPossibleESResult }
     */
    public DpdPossibleESResult createDpdPossibleESResult() {
        return new DpdPossibleESResult();
    }

    /**
     * Create an instance of {@link PossibleExtraService }
     * 
     * @return
     *     the new instance of {@link PossibleExtraService }
     */
    public PossibleExtraService createPossibleExtraService() {
        return new PossibleExtraService();
    }

    /**
     * Create an instance of {@link DpdParcelShopRequest }
     * 
     * @return
     *     the new instance of {@link DpdParcelShopRequest }
     */
    public DpdParcelShopRequest createDpdParcelShopRequest() {
        return new DpdParcelShopRequest();
    }

    /**
     * Create an instance of {@link DpdParcelShops }
     * 
     * @return
     *     the new instance of {@link DpdParcelShops }
     */
    public DpdParcelShops createDpdParcelShops() {
        return new DpdParcelShops();
    }

    /**
     * Create an instance of {@link Limits }
     * 
     * @return
     *     the new instance of {@link Limits }
     */
    public Limits createLimits() {
        return new Limits();
    }

    /**
     * Create an instance of {@link DpdCitiesCashPayRequest }
     * 
     * @return
     *     the new instance of {@link DpdCitiesCashPayRequest }
     */
    public DpdCitiesCashPayRequest createDpdCitiesCashPayRequest() {
        return new DpdCitiesCashPayRequest();
    }

    /**
     * Create an instance of {@link City }
     * 
     * @return
     *     the new instance of {@link City }
     */
    public City createCity() {
        return new City();
    }

    /**
     * Create an instance of {@link StoragePeriodRequest }
     * 
     * @return
     *     the new instance of {@link StoragePeriodRequest }
     */
    public StoragePeriodRequest createStoragePeriodRequest() {
        return new StoragePeriodRequest();
    }

    /**
     * Create an instance of {@link DpdTerminals }
     * 
     * @return
     *     the new instance of {@link DpdTerminals }
     */
    public DpdTerminals createDpdTerminals() {
        return new DpdTerminals();
    }

    /**
     * Create an instance of {@link TerminalStoragePeriods }
     * 
     * @return
     *     the new instance of {@link TerminalStoragePeriods }
     */
    public TerminalStoragePeriods createTerminalStoragePeriods() {
        return new TerminalStoragePeriods();
    }

    /**
     * Create an instance of {@link StoragePeriod }
     * 
     * @return
     *     the new instance of {@link StoragePeriod }
     */
    public StoragePeriod createStoragePeriod() {
        return new StoragePeriod();
    }

    /**
     * Create an instance of {@link ParcelShop.Services }
     * 
     * @return
     *     the new instance of {@link ParcelShop.Services }
     */
    public ParcelShop.Services createParcelShopServices() {
        return new ParcelShop.Services();
    }

    /**
     * Create an instance of {@link TerminalSelf.Services }
     * 
     * @return
     *     the new instance of {@link TerminalSelf.Services }
     */
    public TerminalSelf.Services createTerminalSelfServices() {
        return new TerminalSelf.Services();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WSFault }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link WSFault }{@code >}
     */
    @XmlElementDecl(namespace = "http://dpd.ru/ws/geography/2015-05-20", name = "WSFault")
    public JAXBElement<WSFault> createWSFault(WSFault value) {
        return new JAXBElement<>(_WSFault_QNAME, WSFault.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCitiesCashPay }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetCitiesCashPay }{@code >}
     */
    @XmlElementDecl(namespace = "http://dpd.ru/ws/geography/2015-05-20", name = "getCitiesCashPay")
    public JAXBElement<GetCitiesCashPay> createGetCitiesCashPay(GetCitiesCashPay value) {
        return new JAXBElement<>(_GetCitiesCashPay_QNAME, GetCitiesCashPay.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCitiesCashPayResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetCitiesCashPayResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://dpd.ru/ws/geography/2015-05-20", name = "getCitiesCashPayResponse")
    public JAXBElement<GetCitiesCashPayResponse> createGetCitiesCashPayResponse(GetCitiesCashPayResponse value) {
        return new JAXBElement<>(_GetCitiesCashPayResponse_QNAME, GetCitiesCashPayResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetParcelShops }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetParcelShops }{@code >}
     */
    @XmlElementDecl(namespace = "http://dpd.ru/ws/geography/2015-05-20", name = "getParcelShops")
    public JAXBElement<GetParcelShops> createGetParcelShops(GetParcelShops value) {
        return new JAXBElement<>(_GetParcelShops_QNAME, GetParcelShops.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetParcelShopsResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetParcelShopsResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://dpd.ru/ws/geography/2015-05-20", name = "getParcelShopsResponse")
    public JAXBElement<GetParcelShopsResponse> createGetParcelShopsResponse(GetParcelShopsResponse value) {
        return new JAXBElement<>(_GetParcelShopsResponse_QNAME, GetParcelShopsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPossibleExtraService }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetPossibleExtraService }{@code >}
     */
    @XmlElementDecl(namespace = "http://dpd.ru/ws/geography/2015-05-20", name = "getPossibleExtraService")
    public JAXBElement<GetPossibleExtraService> createGetPossibleExtraService(GetPossibleExtraService value) {
        return new JAXBElement<>(_GetPossibleExtraService_QNAME, GetPossibleExtraService.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPossibleExtraServiceResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetPossibleExtraServiceResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://dpd.ru/ws/geography/2015-05-20", name = "getPossibleExtraServiceResponse")
    public JAXBElement<GetPossibleExtraServiceResponse> createGetPossibleExtraServiceResponse(GetPossibleExtraServiceResponse value) {
        return new JAXBElement<>(_GetPossibleExtraServiceResponse_QNAME, GetPossibleExtraServiceResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetStoragePeriod }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetStoragePeriod }{@code >}
     */
    @XmlElementDecl(namespace = "http://dpd.ru/ws/geography/2015-05-20", name = "getStoragePeriod")
    public JAXBElement<GetStoragePeriod> createGetStoragePeriod(GetStoragePeriod value) {
        return new JAXBElement<>(_GetStoragePeriod_QNAME, GetStoragePeriod.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetStoragePeriodResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetStoragePeriodResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://dpd.ru/ws/geography/2015-05-20", name = "getStoragePeriodResponse")
    public JAXBElement<GetStoragePeriodResponse> createGetStoragePeriodResponse(GetStoragePeriodResponse value) {
        return new JAXBElement<>(_GetStoragePeriodResponse_QNAME, GetStoragePeriodResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTerminalsSelfDelivery2 }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetTerminalsSelfDelivery2 }{@code >}
     */
    @XmlElementDecl(namespace = "http://dpd.ru/ws/geography/2015-05-20", name = "getTerminalsSelfDelivery2")
    public JAXBElement<GetTerminalsSelfDelivery2> createGetTerminalsSelfDelivery2(GetTerminalsSelfDelivery2 value) {
        return new JAXBElement<>(_GetTerminalsSelfDelivery2_QNAME, GetTerminalsSelfDelivery2 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTerminalsSelfDelivery2Response }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetTerminalsSelfDelivery2Response }{@code >}
     */
    @XmlElementDecl(namespace = "http://dpd.ru/ws/geography/2015-05-20", name = "getTerminalsSelfDelivery2Response")
    public JAXBElement<GetTerminalsSelfDelivery2Response> createGetTerminalsSelfDelivery2Response(GetTerminalsSelfDelivery2Response value) {
        return new JAXBElement<>(_GetTerminalsSelfDelivery2Response_QNAME, GetTerminalsSelfDelivery2Response.class, null, value);
    }

}
