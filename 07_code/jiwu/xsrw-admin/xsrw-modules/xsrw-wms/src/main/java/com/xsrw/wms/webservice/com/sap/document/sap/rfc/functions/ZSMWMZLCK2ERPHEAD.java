package com.xsrw.wms.webservice.com.sap.document.sap.rfc.functions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ZSMWMZLCK2ERP_HEAD complex type�� Java �ࡣ
 *
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 *
 * <pre>
 * &lt;complexType name="ZSMWMZLCK2ERP_HEAD">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="WMS_CODE" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="32"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="IVNUM" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="PDATU" type="{urn:sap-com:document:sap:rfc:functions}date" minOccurs="0"/>
 *         &lt;element name="UNAME" minOccurs="0">
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
 *         &lt;element name="WERKS" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="4"/>
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
@XmlType(name = "ZSMWMZLCK2ERP_HEAD", propOrder = {
    "wmscode",
    "ivnum",
    "pdatu",
    "uname",
    "lgnum",
    "werks"
})
public class ZSMWMZLCK2ERPHEAD {

    @XmlElement(name = "WMS_CODE")
    protected String wmscode;
    @XmlElement(name = "IVNUM")
    protected String ivnum;
    @XmlElement(name = "PDATU")
    protected String pdatu;
    @XmlElement(name = "UNAME")
    protected String uname;
    @XmlElement(name = "LGNUM")
    protected String lgnum;
    @XmlElement(name = "WERKS")
    protected String werks;

    /**
     * ��ȡwmscode���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getWMSCODE() {
        return wmscode;
    }

    /**
     * ����wmscode���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setWMSCODE(String value) {
        this.wmscode = value;
    }

    /**
     * ��ȡivnum���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getIVNUM() {
        return ivnum;
    }

    /**
     * ����ivnum���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setIVNUM(String value) {
        this.ivnum = value;
    }

    /**
     * ��ȡpdatu���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPDATU() {
        return pdatu;
    }

    /**
     * ����pdatu���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPDATU(String value) {
        this.pdatu = value;
    }

    /**
     * ��ȡuname���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUNAME() {
        return uname;
    }

    /**
     * ����uname���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUNAME(String value) {
        this.uname = value;
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

}
