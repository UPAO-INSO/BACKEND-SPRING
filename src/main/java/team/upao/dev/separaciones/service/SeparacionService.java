package team.upao.dev.separaciones.service;

import team.upao.dev.separaciones.dto.SeparacionRequestDto;
import team.upao.dev.separaciones.dto.SeparacionResponseDto;
import team.upao.dev.separaciones.dto.SeparacionStatusUpdateDto;

import java.time.LocalDate;
import java.util.List;

public interface SeparacionService {
    SeparacionResponseDto create(SeparacionRequestDto dto);
    SeparacionResponseDto findById(Long id);
    List<SeparacionResponseDto> findByDate(LocalDate date);
    List<SeparacionResponseDto> findToday();
    SeparacionResponseDto changeStatus(Long id, SeparacionStatusUpdateDto dto);
    String cancel(Long id);
}
