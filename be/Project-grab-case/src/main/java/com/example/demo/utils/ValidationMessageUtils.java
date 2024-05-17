package com.example.demo.utils;

import com.example.demo.lib.i18n.utility.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidationMessageUtils {

    @Autowired
    private MessageUtil messageUtil;

    public String generateLoadData() {
        return messageUtil.get("Berhasil memuat makanan");
    }
    public String generateServerErrorMessage() {
        return messageUtil.get("Server eror!!");
    }
    public String generateSuccessAddToCartMessage(){
        return messageUtil.get("Berhasil menambahkan item kedalam keranjang");
    }
    public String generateFailedDataExistCartErrorMessage() {
        return messageUtil.get("Data sudah ada didalam keranjang");
    }
    public String generateFailedCartErrorMessage() {
        return messageUtil.get("Error carts data");
    }

    public String generateSuccessDeleteCartMessage() {
        return messageUtil.get("Berhasil menghapus item didalam keranjang");
    }

    public String generateFailedDeleteCartErrorMessage() {
        return messageUtil.get("Error menghapus item didalam keranjang");
    }

    public String generateSuccessGetDataCartMessage() {
        return messageUtil.get("Berhasil memuat data informasi keranjang");
    }

    public String generateFailedDataCartErrorMessage() {
        return messageUtil.get("Error mendapatkan data informasi keranjang");
    }

    public String generateSuccessCreateOrderMessage() {
        return messageUtil.get("Berhasil membuat orders");
    }

    public String generateErrorCreateOrderMessage() {
        return messageUtil.get("Error membuat orders");
    }

    public String generateSuccessUpdateOrderMessage() {
        return messageUtil.get("Berhasil update orders");
    }

    public String generateOrderNotFoundMessage() {
        return messageUtil.get("order tidak ditemukan");
    }

    public String generateErrorUpdateOrderMessage() {
        return messageUtil.get("Error update orders");
    }

    public String generateSuccessGetOrderIdMessage() {
        return messageUtil.get("Berhasil mendapatkan order id");
    }

    public String generateErrorGetOrderIdMessage() {
        return messageUtil.get("Error mendapatkan order id");
    }

    public String generateSuccessGetOrderByPassenggerIdMessage() {
        return messageUtil.get("Berhasil mendapatkan order berdasarkan passenger Id");
    }

    public String generateErrorGetOrderByPassenggerIdMessage() {
        return messageUtil.get("Error mendapatkan order berdasarkan passenger Id");
    }

    public String generateSuccessGetHistoricalDataOrderMessage() {
        return messageUtil.get("Berhasil mendapatkan Historical data orders");
    }

    public String generateFailedDataHistoricalDataOrderErrorMessage() {
        return messageUtil.get("Error mendapatkan Historical data orders");
    }

    public String generateSuccessGetStatisticDataOrderMessage() {
        return messageUtil.get("Berhasil mendapatkan statistik data orders");
    }

    public String generateFailedDataStatisticsOrderErrorMessage() {
        return messageUtil.get("Error mendapatkan statistik data orders");
    }
}




























