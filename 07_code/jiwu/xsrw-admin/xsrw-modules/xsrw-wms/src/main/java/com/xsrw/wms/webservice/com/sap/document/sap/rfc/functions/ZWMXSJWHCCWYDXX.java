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
 *                   &lt;element name="item" type="{urn:sap-com:document:sap:rfc:functions}ZSWM_YDD2ERP_HCCWYDXX" maxOccurs="unbounded" minOccurs="0"/>
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
 *                   &lt;element name="item" type="{urn:sap-com:document:sap:rfc:functions}ZSWM_YDD2ERP_HCCWYDXX_RE" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlRootElement(name = "Z_WM_XSJW_HCCWYDXX")
public class ZWMXSJWHCCWYDXX {

    @XmlElement(name = "INPUT")
    protected ZWMXSJWHCCWYDXX.INPUT input;
    @XmlElement(name = "OUTPUT")
    protected ZWMXSJWHCCWYDXX.OUTPUT output;

    /**
     * ��ȡinput���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ZWMXSJWHCCWYDXX.INPUT }
     *     
     */
    public ZWMXSJWHCCWYDXX.INPUT getINPUT() {
        return input;
    }

    /**
     * ����input���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ZWMXSJWHCCWYDXX.INPUT }
     *     
     */
    public void setINPUT(ZWMXSJWHCCWYDXX.INPUT value) {
        this.input = value;
    }

    /**
     * ��ȡoutput���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ZWMXSJWHCCWYDXX.OUTPUT }
     *     
     */
    public ZWMXSJWHCCWYDXX.OUTPUT getOUTPUT() {
        return output;
    }

    /**
     * ����output���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ZWMXSJWHCCWYDXX.OUTPUT }
     *     
     */
    public void setOUTPUT(ZWMXSJWHCCWYDXX.OUTPUT value) {
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
     *         &lt;element name="item" type="{urn:sap-com:document:sap:rfc:functions}ZSWM_YDD2ERP_HCCWYDXX" maxOccurs="unbounded" minOccurs="0"/>
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

        protected List<ZSWMYDD2ERPHCCWYDXX> item;

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
         * {@link ZSWMYDD2ERPHCCWYDXX }
         * 
         * 
         */
        public List<ZSWMYDD2ERPHCCWYDXX> getItem() {
            if (item == null) {
                item = new ArrayList<ZSWMYDD2ERPHCCWYDXX>();
            }
            return this.item;
        }

        public void setItem(List<ZSWMYDD2ERPHCCWYDXX> item) {
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
     *         &lt;element name="item" type="{urn:sap-com:document:sap:rfc:functions}ZSWM_YDD2ERP_HCCWYDXX_RE" maxOccurs="unbounded" minOccurs="0"/>
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

        protected List<ZSWMYDD2ERPHCCWYDXXRE> item;

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
         * {@link ZSWMYDD2ERPHCCWYDXXRE }
         * 
         * 
         */
        public List<ZSWMYDD2ERPHCCWYDXXRE> getItem() {
            if (item == null) {
                item = new ArrayList<ZSWMYDD2ERPHCCWYDXXRE>();
            }
            return this.item;
        }

        public void setItem(List<ZSWMYDD2ERPHCCWYDXXRE> item) {
            this.item = item;
        }

    }

}
