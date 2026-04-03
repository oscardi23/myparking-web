package com.devsoft.myparking.services;


import com.devsoft.myparking.dtos.ClientDTO;
import com.devsoft.myparking.models.Client;
import com.devsoft.myparking.repository.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService{

    private final ClientRepository clientRepository;
    @Override
    public ClientDTO createClient(ClientDTO clientDTO, String parkingID) {

    if (clientRepository.existsByNationalIdAndParkingId(clientDTO.getNationalId(), parkingID)){
        throw new RuntimeException("Ya existe un cliente con ese numero de cedula en este parqueadero");
    }

        Client client = new Client();

        client.setParkingId(parkingID);
        client.setName(clientDTO.getName());
        client.setLastName(client.getLastName());
        client.setNationalId(client.getNationalId());
        client.setPhone(client.getPhone());
        client.setEmail(client.getEmail());
        client.setActive(true);
        client.setCreatedAt(LocalDateTime.now());
        client.setUpdateAt(LocalDateTime.now());

        Client saved = clientRepository.save(client);


        return convertToDTO(saved);
    }

    private ClientDTO convertToDTO(Client client) {

        ClientDTO dto = new ClientDTO();

        dto.setId(client.getId());
        dto.setParkingID(client.getParkingId());
        dto.setName(client.getName());
        dto.setLastName(client.getLastName());
        dto.setNationalId(client.getNationalId());
        dto.setPhone(client.getPhone());
        dto.setEmail(client.getEmail());
        dto.setActive(client.isActive());
        return dto;


    }

    @Override
    public ClientDTO getClient(String clientId) {
        return clientRepository.findById(clientId)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }

    @Override
    public List<ClientDTO> getClientsByParkingId(String parkingID) {
        return clientRepository.findByParkingId(parkingID)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public ClientDTO updateClient(ClientDTO clientDTO) {
        Client client = clientRepository.findById(clientDTO.getId()).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        client.setLastName(clientDTO.getLastName());
        client.setNationalId(clientDTO.getNationalId());
        client.setPhone(clientDTO.getPhone());
        client.setEmail(clientDTO.getEmail());
        client.setUpdateAt(LocalDateTime.now());

        return convertToDTO(clientRepository.save(client));

    }

    @Override
    public void toggleActive(String clientId) {

        Client client = clientRepository.findById(clientId).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        client.setActive(!client.isActive());
        client.setUpdateAt(LocalDateTime.now());

        clientRepository.save(client);

    }
}
