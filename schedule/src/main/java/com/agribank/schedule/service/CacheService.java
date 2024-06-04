package com.agribank.schedule.service;


import com.agribank.schedule.dto.CacheDTO;
import com.agribank.schedule.dto.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.NoResultException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public interface CacheService {
    ResponseDTO<Set<String>> getCacheNames();

    void clearCache(CacheDTO cacheDTO);

    ResponseDTO<Set<String>> getKeysByCacheName(String name);
}

@Service
class CacheServiceImpl implements  CacheService {
    @Autowired
    CacheManager cacheManager;

    @Override
    public void clearCache(CacheDTO cacheDTO) {
        Cache cache = cacheManager.getCache(cacheDTO.getName());

        if (cache != null)
            if (StringUtils.hasText(cacheDTO.getKey()))
                cache.evict(cacheDTO.getKey());
            else
                cache.clear();

    }

    @Override
    public ResponseDTO<Set<String>> getCacheNames() {
        Set<String> cacheNames = cacheManager.getCacheNames().stream().collect(Collectors.toSet());
        return ResponseDTO.<Set<String>>builder().code(String.valueOf(HttpStatus.OK.value())).data(cacheNames)
                .totalElements((long) cacheNames.size()).totalPages(1L).numberOfElements((long) cacheNames.size())
                .build();
    }

    @Override
    public ResponseDTO<Set<String>> getKeysByCacheName(String name) {
        Cache cache = cacheManager.getCache(name);

        if (cache == null)
            throw new NoResultException();

        @SuppressWarnings("unchecked")
        Set<String> keySet = ((Map<Object, Object>) cache.getNativeCache()).keySet().stream().map(key -> key.toString()).collect(Collectors.toSet());

        return ResponseDTO.<Set<String>>builder().code(String.valueOf(HttpStatus.OK.value())).data(keySet)
                .totalElements((long) keySet.size()).totalPages(1L).numberOfElements((long) keySet.size()).build();
    }
}
