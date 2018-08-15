package ru.zekoh.db.DAO;

import ru.zekoh.db.entity.SellReport;

import java.util.List;

public interface SellReportDao {
    public List<SellReport> findAll();
}
