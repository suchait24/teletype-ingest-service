package com.infogain.gcp.poc.consumer.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.*;

@Getter
@Setter
@ToString
@XmlRootElement(name = "Message")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"Timestamp", "AddressLine", "Origin", "MessageCorrelationID", "MessageIdentity", "StandardMessageIdentifier", "MessageType", "MessageBody"})
public class TeletypeEventDTO {

    @XmlElement(name="Timestamp")
    private  String Timestamp;

    @XmlElement(name="AddressLine")
    private AddressLine AddressLine;

    @XmlElement(name="Origin")
    private  String Origin;

    @XmlElement(name="MessageCorrelationID")
    private  String MessageCorrelationID;

    @XmlElement(name="MessageIdentity")
    private  String MessageIdentity;

    @XmlElement(name="StandardMessageIdentifier")
    private  String StandardMessageIdentifier;

    @XmlElement(name="MessageType")
    private  String MessageType;

    @XmlElement(name="MessageBody")
    private MessageBody MessageBody;
}
