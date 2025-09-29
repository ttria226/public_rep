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
 *         &lt;element name="I_HEAD" type="{urn:sap-com:document:sap:rfc:functions}ZSMWMZLCK2ERP_HEAD" minOccurs="0"/>
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
@XmlRootElement(name = "Z_WM_XSJW2ERP_RECEIVE")
public class ZWMXSJW2ERPRECEIVE {

    @XmlElement(name = "I_HEAD")
    protected ZSMWMZLCK2ERPHEAD ihead;
    @XmlElement(name = "T_ITEM", required = true)
    protected ZWMXSJW2ERPRECEIVE.TITEM titem;

    /**
     * ��ȡihead���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link ZSMWMZLCK2ERPHEAD }
     *
     */
    public ZSMWMZLCK2ERPHEAD getIHEAD() {
        return ihead;
    }

    /**
     * ����ihead���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link ZSMWMZLCK2ERPHEAD }
     *
     */
    public void setIHEAD(ZSMWMZLCK2ERPHEAD value) {
        this.ihead = value;
    }

    /**
     * ��ȡtitem���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link ZWMXSJW2ERPRECEIVE.TITEM }
     *
     */
    public ZWMXSJW2ERPRECEIVE.TITEM getTITEM() {
        return titem;
    }

    /**
     * ����titem���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link ZWMXSJW2ERPRECEIVE.TITEM }
     *
     */
    public void setTITEM(ZWMXSJW2ERPRECEIVE.TITEM value) {
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

        public void setItem(List<ZSWMXSWM2ERPITEM> item) {
            this.item = item;
        }
    }

}
