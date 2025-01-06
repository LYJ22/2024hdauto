package com.hd.v1.app.cust.service;

import com.hd.common.exception.DataNotFoundException;
import com.hd.common.exception.ErrorCode;
import com.hd.common.exception.IdNotFoundException;
import com.hd.common.frame.HDService;
import com.hd.v1.app.cust.repository.CustRepository;
import com.hd.v1.common.entity.CustEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustService implements HDService<CustEntity, String> {
    final CustRepository custRepository;

    @Override
    public CustEntity get(String s) {
        CustEntity custEntity = custRepository.findById(s).orElseThrow(()->
                new IdNotFoundException(ErrorCode.ID_NOT_FOUND.getErrorMessage(), ErrorCode.ID_NOT_FOUND));
        return custEntity;
    }

    @Override
    public CustEntity save(CustEntity custEntity) {
        if(custEntity.getPwd() == null || custEntity.getName() == null){
            throw new DataNotFoundException(ErrorCode.DATA_DOSE_NOT_EXIST.getErrorMessage(), ErrorCode.DATA_DOSE_NOT_EXIST);
        }
        return custRepository.save(custEntity);
    }

    @Override
    public CustEntity modify(CustEntity custEntity) {
        custRepository.findById(custEntity.getId()).orElseThrow(()->
                new IdNotFoundException(ErrorCode.ID_NOT_FOUND.getErrorMessage(), ErrorCode.ID_NOT_FOUND));
        return custRepository.save(custEntity);
    }

    @Override
    public String remove(String s) {
        custRepository.findById(s).orElseThrow(()->
                new IdNotFoundException(ErrorCode.ID_NOT_FOUND.getErrorMessage(), ErrorCode.ID_NOT_FOUND));
        custRepository.deleteById(s);
        return s;
    }

    @Override
    public List<CustEntity> getall() {
        List<CustEntity> list = custRepository.findAll();
        if (list.isEmpty()) {
            throw new DataNotFoundException(ErrorCode.DATA_DOSE_NOT_EXIST.getErrorMessage(), ErrorCode.DATA_DOSE_NOT_EXIST);
        }
        return list;
    }
}
