package com.infogain.gcp.poc.consumer.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@XmlRootElement(name = "TeletypEvent")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"messageCorelationId", "carrierCode", "hostRecordLocator"})
public class TeletypeEventDTO {

    @XmlElement(name = "messageCorelationId")
    private String messageCorelationId;

    @XmlElement(name = "carrierCode")
    private String carrierCode;

    @XmlElement(name = "hostRecordLocator")
    private String hostRecordLocator;
}
