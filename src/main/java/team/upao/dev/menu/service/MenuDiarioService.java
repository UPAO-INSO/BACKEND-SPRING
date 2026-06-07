package team.upao.dev.menu.service;

import team.upao.dev.menu.dto.MenuDiarioItemRequestDto;
import team.upao.dev.menu.dto.MenuDiarioItemResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface MenuDiarioService {
    /** Crea o actualiza las porciones de varios productos para hoy (upsert bulk). */
    List<MenuDiarioItemResponseDto> saveForToday(List<MenuDiarioItemRequestDto> items);

    /** Retorna los ítems del menú del día (con porciones). */
    List<MenuDiarioItemResponseDto> findToday();

    List<MenuDiarioItemResponseDto> findByDate(LocalDate date);

    /** Actualiza solo las porciones estimadas de un ítem existente. */
    MenuDiarioItemResponseDto updatePortions(Long id, Integer estimatedPortions);

    /** Elimina un producto del menú del día. */
    String remove(Long id);
}
