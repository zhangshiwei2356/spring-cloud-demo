package com.cloud.business.demo;

import com.cloud.business.demo.entity.CompanyRecord;
import com.cloud.business.demo.entity.DemoOrderRecord;
import com.cloud.business.demo.entity.DemoUserRecord;
import com.cloud.business.demo.entity.DepartmentRecord;
import com.cloud.business.demo.entity.ProductRecord;
import com.cloud.common.base.BaseController;
import com.cloud.common.domain.Result;
import com.cloud.common.exception.GlobalException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/demo")
@Tag(name = "演示数据", description = "公司/部门/用户/产品/订单 JSON 文件持久化")
public class DemoDataController extends BaseController<Object, Object> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final DemoDataRegistry registry;

    public DemoDataController(DemoDataRegistry registry) {
        this.registry = registry;
    }

    @GetMapping("/storage-path")
    @Operation(summary = "数据存储目录")
    public Result<String> storagePath() {
        return ok(registry.rootDirPath());
    }

    @GetMapping("/{resource}")
    @Operation(summary = "列表")
    public Result<List<?>> list(@PathVariable("resource") String resource) {
        requireResource(resource);
        return ok(registry.get(resource).list());
    }

    @GetMapping("/{resource}/{id}")
    @Operation(summary = "详情")
    public Result<?> get(@PathVariable("resource") String resource, @PathVariable("id") Long id) {
        requireResource(resource);
        return ok(registry.get(resource).get(id));
    }

    @PostMapping("/{resource}")
    @Operation(summary = "新增")
    public Result<?> create(@PathVariable("resource") String resource, @RequestBody Map<String, Object> body) {
        requireResource(resource);
        return ok(doCreate(resource, body));
    }

    @PutMapping("/{resource}/{id}")
    @Operation(summary = "修改")
    public Result<?> update(@PathVariable("resource") String resource,
                            @PathVariable("id") Long id,
                            @RequestBody Map<String, Object> body) {
        requireResource(resource);
        return ok(doUpdate(resource, id, body));
    }

    @DeleteMapping("/{resource}/{id}")
    @Operation(summary = "删除")
    public Result<Void> delete(@PathVariable("resource") String resource, @PathVariable("id") Long id) {
        requireResource(resource);
        registry.get(resource).delete(id);
        return ok();
    }

    @PostMapping("/{resource}/reset")
    @Operation(summary = "重置为种子数据")
    public Result<List<?>> reset(@PathVariable("resource") String resource) {
        requireResource(resource);
        return ok(registry.get(resource).reset());
    }

    private void requireResource(String resource) {
        if (!registry.contains(resource)) {
            throw new GlobalException("未知资源类型: " + resource);
        }
    }

    private Object doCreate(String resource, Map<String, Object> body) {
        return switch (resource) {
            case "companies" -> {
                CompanyRecord r = MAPPER.convertValue(body, CompanyRecord.class);
                validateCompany(r);
                yield registry.<CompanyRecord>get(resource).create(r);
            }
            case "departments" -> {
                DepartmentRecord r = MAPPER.convertValue(body, DepartmentRecord.class);
                validateDepartment(r);
                yield registry.<DepartmentRecord>get(resource).create(r);
            }
            case "users" -> {
                DemoUserRecord r = MAPPER.convertValue(body, DemoUserRecord.class);
                validateUser(r);
                yield registry.<DemoUserRecord>get(resource).create(r);
            }
            case "products" -> {
                ProductRecord r = MAPPER.convertValue(body, ProductRecord.class);
                validateProduct(r);
                yield registry.<ProductRecord>get(resource).create(r);
            }
            case "orders" -> {
                DemoOrderRecord r = MAPPER.convertValue(body, DemoOrderRecord.class);
                validateOrder(r);
                yield registry.<DemoOrderRecord>get(resource).create(r);
            }
            default -> throw new GlobalException("未知资源类型: " + resource);
        };
    }

    private Object doUpdate(String resource, Long id, Map<String, Object> body) {
        return switch (resource) {
            case "companies" -> {
                CompanyRecord r = MAPPER.convertValue(body, CompanyRecord.class);
                validateCompany(r);
                yield registry.<CompanyRecord>get(resource).update(id, r);
            }
            case "departments" -> {
                DepartmentRecord r = MAPPER.convertValue(body, DepartmentRecord.class);
                validateDepartment(r);
                yield registry.<DepartmentRecord>get(resource).update(id, r);
            }
            case "users" -> {
                DemoUserRecord r = MAPPER.convertValue(body, DemoUserRecord.class);
                validateUser(r);
                yield registry.<DemoUserRecord>get(resource).update(id, r);
            }
            case "products" -> {
                ProductRecord r = MAPPER.convertValue(body, ProductRecord.class);
                validateProduct(r);
                yield registry.<ProductRecord>get(resource).update(id, r);
            }
            case "orders" -> {
                DemoOrderRecord r = MAPPER.convertValue(body, DemoOrderRecord.class);
                validateOrder(r);
                yield registry.<DemoOrderRecord>get(resource).update(id, r);
            }
            default -> throw new GlobalException("未知资源类型: " + resource);
        };
    }

    private static void validateCompany(CompanyRecord r) {
        if (DemoCrudService.isBlank(r.getCode()) || DemoCrudService.isBlank(r.getName())) {
            throw new GlobalException("公司编码和名称不能为空");
        }
    }

    private static void validateDepartment(DepartmentRecord r) {
        if (DemoCrudService.isBlank(r.getCompanyName()) || DemoCrudService.isBlank(r.getName())) {
            throw new GlobalException("所属公司和部门名称不能为空");
        }
    }

    private static void validateUser(DemoUserRecord r) {
        if (DemoCrudService.isBlank(r.getLoginName()) || DemoCrudService.isBlank(r.getUserName())) {
            throw new GlobalException("登录名和姓名不能为空");
        }
    }

    private static void validateProduct(ProductRecord r) {
        if (DemoCrudService.isBlank(r.getCode()) || DemoCrudService.isBlank(r.getName())) {
            throw new GlobalException("SKU 编码和产品名称不能为空");
        }
        if (DemoCrudService.isBlank(r.getPrice())) {
            throw new GlobalException("单价不能为空");
        }
    }

    private static void validateOrder(DemoOrderRecord r) {
        if (DemoCrudService.isBlank(r.getBuyerName())
                || DemoCrudService.isBlank(r.getProductCode())
                || DemoCrudService.isBlank(r.getProductName())) {
            throw new GlobalException("买家、SKU 和商品名称不能为空");
        }
        if (DemoCrudService.isBlank(r.getAmount())) {
            throw new GlobalException("金额不能为空");
        }
    }
}
