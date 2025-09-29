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
 *         &lt;element name="INPUT" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="item" type="{urn:sap-com:document:sap:rfc:functions}ZSWM_YDD2ERP_HCZCSQXX_SWAQFD" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="OUTPUT" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="item" type="{urn:sap-com:document:sap:rfc:functions}ZSWM_YDD2ERP_HCZCSQXX_RE" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlRootElement(name = "Z_WM_XSJW_HCZCSQXX")
public class ZWMXSJWHCZCSQXX {

    @XmlElement(name = "INPUT")
    protected ZWMXSJWHCZCSQXX.INPUT input;
    @XmlElement(name = "OUTPUT")
    protected ZWMXSJWHCZCSQXX.OUTPUT output;

    /**
     * ��ȡinput���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link ZWMXSJWHCZCSQXX.INPUT }
     *
     */
    public ZWMXSJWHCZCSQXX.INPUT getINPUT() {
        return input;
    }

    /**
     * ����input���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link ZWMXSJWHCZCSQXX.INPUT }
     *
     */
    public void setINPUT(ZWMXSJWHCZCSQXX.INPUT value) {
        this.input = value;
    }

    /**
     * ��ȡoutput���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link ZWMXSJWHCZCSQXX.OUTPUT }
     *
     */
    public ZWMXSJWHCZCSQXX.OUTPUT getOUTPUT() {
        return output;
    }

    /**
     * ����output���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link ZWMXSJWHCZCSQXX.OUTPUT }
     *
     */
    public void setOUTPUT(ZWMXSJWHCZCSQXX.OUTPUT value) {
        this.output = value;
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
     *         &lt;element name="item" type="{urn:sap-com:document:sap:rfc:functions}ZSWM_YDD2ERP_HCZCSQXX_SWAQFD" maxOccurs="unbounded" minOccurs="0"/>
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
    public static class INPUT {

        protected List<ZSWMYDD2ERPHCZCSQXXSWAQFD> item;

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
         * {@link ZSWMYDD2ERPHCZCSQXXSWAQFD }
         *
         *
         */
        public List<ZSWMYDD2ERPHCZCSQXXSWAQFD> getItem() {
            if (item == null) {
                item = new ArrayList<ZSWMYDD2ERPHCZCSQXXSWAQFD>();
            }
            return this.item;
        }

        public void setItem(List<ZSWMYDD2ERPHCZCSQXXSWAQFD> item) {
            this.item = item;
        }
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
     *         &lt;element name="item" type="{urn:sap-com:document:sap:rfc:functions}ZSWM_YDD2ERP_HCZCSQXX_RE" maxOccurs="unbounded" minOccurs="0"/>
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
    public static class OUTPUT {

        protected List<ZSWMYDD2ERPHCZCSQXXRE> item;

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
         * {@link ZSWMYDD2ERPHCZCSQXXRE }
         *
         *
         */
        public List<ZSWMYDD2ERPHCZCSQXXRE> getItem() {
            if (item == null) {
                item = new ArrayList<ZSWMYDD2ERPHCZCSQXXRE>();
            }
            return this.item;
        }

        public void setItem(List<ZSWMYDD2ERPHCZCSQXXRE> item) {
            this.item = item;
        }

    }

}
