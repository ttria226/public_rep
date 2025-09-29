package com.xsrw.wms.webservice.com.sap.document.sap.rfc.functions;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ZSWM_YDD2ERP_HCZCSQXX_SWAQFD complex type�� Java �ࡣ
 *
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 *
 * <pre>
 * &lt;complexType name="ZSWM_YDD2ERP_HCZCSQXX_SWAQFD">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="WERKS" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="LGORT" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ZZYHZH" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="12"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="LGNUM" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="TRART" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="TBNUM" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
 *               &lt;pattern value="\d+"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="TBPOS" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="6"/>
 *               &lt;pattern value="\d+"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="MATNR" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="CHARG" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="BESTQ" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="SOBKZ" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="PSPNR" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="24"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ZZWZTM" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ZZCWTM" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="MENGE" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="13"/>
 *               &lt;fractionDigits value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="CONFORM" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ZZYL1" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ZZYL2" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ZZYL3" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ZZYL4" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZSWM_YDD2ERP_HCZCSQXX_SWAQFD", propOrder = {
    "werks",
    "lgort",
    "zzyhzh",
    "lgnum",
    "trart",
    "tbnum",
    "tbpos",
    "matnr",
    "charg",
    "bestq",
    "sobkz",
    "pspnr",
    "zzwztm",
    "zzcwtm",
    "menge",
    "conform",
    "zzyl1",
    "zzyl2",
    "zzyl3",
    "zzyl4"
})
public class ZSWMYDD2ERPHCZCSQXXSWAQFD {

    @XmlElement(name = "WERKS")
    protected String werks;
    @XmlElement(name = "LGORT")
    protected String lgort;
    @XmlElement(name = "ZZYHZH")
    protected String zzyhzh;
    @XmlElement(name = "LGNUM")
    protected String lgnum;
    @XmlElement(name = "TRART")
    protected String trart;
    @XmlElement(name = "TBNUM")
    protected String tbnum;
    @XmlElement(name = "TBPOS")
    protected String tbpos;
    @XmlElement(name = "MATNR")
    protected String matnr;
    @XmlElement(name = "CHARG")
    protected String charg;
    @XmlElement(name = "BESTQ")
    protected String bestq;
    @XmlElement(name = "SOBKZ")
    protected String sobkz;
    @XmlElement(name = "PSPNR")
    protected String pspnr;
    @XmlElement(name = "ZZWZTM")
    protected String zzwztm;
    @XmlElement(name = "ZZCWTM")
    protected String zzcwtm;
    @XmlElement(name = "MENGE")
    protected BigDecimal menge;
    @XmlElement(name = "CONFORM")
    protected String conform;
    @XmlElement(name = "ZZYL1")
    protected String zzyl1;
    @XmlElement(name = "ZZYL2")
    protected String zzyl2;
    @XmlElement(name = "ZZYL3")
    protected String zzyl3;
    @XmlElement(name = "ZZYL4")
    protected String zzyl4;

    /**
     * ��ȡwerks���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getWERKS() {
        return werks;
    }

    /**
     * ����werks���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setWERKS(String value) {
        this.werks = value;
    }

    /**
     * ��ȡlgort���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getLGORT() {
        return lgort;
    }

    /**
     * ����lgort���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setLGORT(String value) {
        this.lgort = value;
    }

    /**
     * ��ȡzzyhzh���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getZZYHZH() {
        return zzyhzh;
    }

    /**
     * ����zzyhzh���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setZZYHZH(String value) {
        this.zzyhzh = value;
    }

    /**
     * ��ȡlgnum���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getLGNUM() {
        return lgnum;
    }

    /**
     * ����lgnum���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setLGNUM(String value) {
        this.lgnum = value;
    }

    /**
     * ��ȡtrart���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTRART() {
        return trart;
    }

    /**
     * ����trart���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTRART(String value) {
        this.trart = value;
    }

    /**
     * ��ȡtbnum���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTBNUM() {
        return tbnum;
    }

    /**
     * ����tbnum���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTBNUM(String value) {
        this.tbnum = value;
    }

    /**
     * ��ȡtbpos���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTBPOS() {
        return tbpos;
    }

    /**
     * ����tbpos���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTBPOS(String value) {
        this.tbpos = value;
    }

    /**
     * ��ȡmatnr���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMATNR() {
        return matnr;
    }

    /**
     * ����matnr���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMATNR(String value) {
        this.matnr = value;
    }

    /**
     * ��ȡcharg���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCHARG() {
        return charg;
    }

    /**
     * ����charg���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCHARG(String value) {
        this.charg = value;
    }

    /**
     * ��ȡbestq���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getBESTQ() {
        return bestq;
    }

    /**
     * ����bestq���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setBESTQ(String value) {
        this.bestq = value;
    }

    /**
     * ��ȡsobkz���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSOBKZ() {
        return sobkz;
    }

    /**
     * ����sobkz���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSOBKZ(String value) {
        this.sobkz = value;
    }

    /**
     * ��ȡpspnr���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPSPNR() {
        return pspnr;
    }

    /**
     * ����pspnr���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPSPNR(String value) {
        this.pspnr = value;
    }

    /**
     * ��ȡzzwztm���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getZZWZTM() {
        return zzwztm;
    }

    /**
     * ����zzwztm���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setZZWZTM(String value) {
        this.zzwztm = value;
    }

    /**
     * ��ȡzzcwtm���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getZZCWTM() {
        return zzcwtm;
    }

    /**
     * ����zzcwtm���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setZZCWTM(String value) {
        this.zzcwtm = value;
    }

    /**
     * ��ȡmenge���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getMENGE() {
        return menge;
    }

    /**
     * ����menge���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setMENGE(BigDecimal value) {
        this.menge = value;
    }

    /**
     * ��ȡconform���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCONFORM() {
        return conform;
    }

    /**
     * ����conform���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCONFORM(String value) {
        this.conform = value;
    }

    /**
     * ��ȡzzyl1���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getZZYL1() {
        return zzyl1;
    }

    /**
     * ����zzyl1���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setZZYL1(String value) {
        this.zzyl1 = value;
    }

    /**
     * ��ȡzzyl2���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getZZYL2() {
        return zzyl2;
    }

    /**
     * ����zzyl2���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setZZYL2(String value) {
        this.zzyl2 = value;
    }

    /**
     * ��ȡzzyl3���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getZZYL3() {
        return zzyl3;
    }

    /**
     * ����zzyl3���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setZZYL3(String value) {
        this.zzyl3 = value;
    }

    /**
     * ��ȡzzyl4���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getZZYL4() {
        return zzyl4;
    }

    /**
     * ����zzyl4���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setZZYL4(String value) {
        this.zzyl4 = value;
    }

}
