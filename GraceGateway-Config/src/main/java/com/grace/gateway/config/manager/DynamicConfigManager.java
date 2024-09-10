package com.grace.gateway.config.manager;

import com.grace.gateway.config.pojo.RouteDefinition;
import com.grace.gateway.config.pojo.ServiceDefinition;
import com.grace.gateway.config.pojo.ServiceInstance;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动态配置管理，缓存从配置中心拉取下来的配置
 */
public class DynamicConfigManager {

    // 路由id对应的路由
    private final ConcurrentHashMap<String /* 路由id */, RouteDefinition> routeId2RouteMap = new ConcurrentHashMap<>();

    // 服务对应的路由
    private final ConcurrentHashMap<String /* 服务名 */, RouteDefinition> serviceName2RouteMap = new ConcurrentHashMap<>();

    // 服务
    private final ConcurrentHashMap<String /* 服务名 */, ServiceDefinition> serviceDefinitionMap = new ConcurrentHashMap<>();

    // 服务对应的实例
    private final ConcurrentHashMap<String /* 服务名 */, ConcurrentHashMap<String /* 实例id */, ServiceInstance>> serviceInstanceMap = new ConcurrentHashMap<>();

    /*********   单例   *********/
    private DynamicConfigManager() {}

    private static final DynamicConfigManager INSTANCE = new DynamicConfigManager();

    public DynamicConfigManager getInstance() {
        return INSTANCE;
    }

    /*********   路由   *********/
    public void updateRouteByRouteId(String id, RouteDefinition routeDefinition) {
        routeId2RouteMap.put(id, routeDefinition);
    }

    public void updateRoutes(Collection<RouteDefinition> routes) {
        updateRoutes(routes, false);
    }

    public void updateRoutes(Collection<RouteDefinition> routes, boolean clear) {
        if (routes == null || routes.isEmpty()) return;
        if (clear) {
            routeId2RouteMap.clear();
            serviceName2RouteMap.clear();
        }
        for (RouteDefinition route : routes) {
            if (route == null) continue;
            routeId2RouteMap.put(route.getId(), route);
            serviceName2RouteMap.put(route.getServiceName(), route);
        }
    }

    public RouteDefinition getRouteById(String id) {
        return routeId2RouteMap.get(id);
    }

    public RouteDefinition getRouteByServiceName(String serviceName) {
        return serviceName2RouteMap.get(serviceName);
    }

    /*********   服务   *********/
    public void updateServiceByName(String name, ServiceDefinition serviceDefinition) {
        serviceDefinitionMap.put(name, serviceDefinition);
    }

    public void updateServices(Collection<ServiceDefinition> services) {
        updateServices(services, false);
    }

    public void updateServices(Collection<ServiceDefinition> services, boolean clear) {
        if (services == null || services.isEmpty()) return;
        if (clear) {
            serviceDefinitionMap.clear();
        }
        for (ServiceDefinition service : services) {
            if (service == null) continue;
            serviceDefinitionMap.put(service.getServiceName(), service);
        }
    }

    public ServiceDefinition getServiceByName(String name) {
        return serviceDefinitionMap.get(name);
    }

    /*********   实例   *********/
    public void addServiceInstance(String serviceName, ServiceInstance instance) {
        serviceInstanceMap.computeIfAbsent(serviceName, k -> new ConcurrentHashMap<>()).put(instance.getInstanceId(), instance);
    }

    public void removeServiceInstance(String serviceName, ServiceInstance instance) {
        serviceInstanceMap.compute(serviceName, (k, v) -> {
            if (v == null || v.get(instance.getInstanceId()) == null) return v;
            v.remove(instance.getInstanceId());
            return v;
        });
    }

    public Map<String, ServiceInstance> getInstancesByServiceName(String serviceName) {
        return serviceInstanceMap.get(serviceName);
    }

}