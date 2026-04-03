package com.devsoft.myparking.services;

import com.devsoft.myparking.dtos.ClientDTO;

import java.util.List;

public interface ClientService {

    ClientDTO createClient(ClientDTO clientDTO, String parkingID);

    ClientDTO getClient(String clientId);

    List<ClientDTO> getClientsByParkingId(String parkingID);

    ClientDTO updateClient(ClientDTO clientDTO);

    void toggleActive(String clientId);
}
