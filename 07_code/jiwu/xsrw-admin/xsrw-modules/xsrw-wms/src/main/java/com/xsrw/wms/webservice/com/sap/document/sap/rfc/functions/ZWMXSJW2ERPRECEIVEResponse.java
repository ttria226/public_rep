package com.xsrw.wms.webservice.com.sap.document.sap.rfc.functions;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type�� Java �ࡣ
 *
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="E_RETURN" type="{urn:sap-com:document:sap:rfc:functions}ZSMWMZLCK2ERP_RETURN" minOccurs="0"/>
 *         &lt;element name="T_ITEM">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="item" type="{urn:sap-com:document:sap:rfc:functions}ZSWM_XSWM2ERP_ITEM" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "Z_WM_XSJW2ERP_RECEIVE.Response")
public class ZWMXSJW2ERPRECEIVEResponse {

    @XmlElement(name = "E_RETURN")
    protected ZSMWMZLCK2ERPRETURN ereturn;
    @XmlElement(name = "T_ITEM", required = true)
    protected ZWMXSJW2ERPRECEIVEResponse.TITEM titem;

    /**
     * ��ȡereturn���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link ZSMWMZLCK2ERPRETURN }
     *
     */
    public ZSMWMZLCK2ERPRETURN getERETURN() {
        return ereturn;
    }

    /**
     * ����ereturn���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link ZSMWMZLCK2ERPRETURN }
     *
     */
    public void setERETURN(ZSMWMZLCK2ERPRETURN value) {
        this.ereturn = value;
    }

    /**
     * ��ȡtitem���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link ZWMXSJW2ERPRECEIVEResponse.TITEM }
     *
     */
    public ZWMXSJW2ERPRECEIVEResponse.TITEM getTITEM() {
        return titem;
    }

    /**
     * ����titem���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link ZWMXSJW2ERPRECEIVEResponse.TITEM }
     *
     */
    public void setTITEM(ZWMXSJW2ERPRECEIVEResponse.TITEM value) {
        this.titem = value;
    }


    /**
     * <p>anonymous complex type�� Java �ࡣ
     *
     * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
     *
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="item" type="{urn:sap-com:document:sap:rfc:functions}ZSWM_XSWM2ERP_ITEM" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     *
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "item"
    })
    public static class TITEM {

        protected List<ZSWMXSWM2ERPITEM> item;

        /**
         * Gets the value of the item property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the item property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getItem().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ZSWMXSWM2ERPITEM }
         *
         *
         */
        public List<ZSWMXSWM2ERPITEM> getItem() {
            if (item == null) {
                item = new ArrayList<ZSWMXSWM2ERPITEM>();
            }
            return this.item;
        }

    }

}
