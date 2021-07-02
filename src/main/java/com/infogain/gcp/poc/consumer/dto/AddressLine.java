package com.infogain.gcp.poc.consumer.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Getter
@Setter
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"Priority", "Destination"})
public class AddressLine {
    @XmlElement(name="Priority")
    private String Priority;
    @XmlElement(name="Destination")
    private String Destination;
}
