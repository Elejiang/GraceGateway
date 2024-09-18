package com.grace.gateway.config.manager;

import com.grace.gateway.config.pojo.RouteDefinition;
import com.grace.gateway.config.pojo.ServiceDefinition;
import com.grace.gateway.config.pojo.ServiceInstance;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 动态配置管理，缓存从配置中心拉取下来的配置
 */
public class DynamicConfigManager {

    private static final DynamicConfigManager INSTANCE = new DynamicConfigManager();
    // 路由规则变化监听器
    private final ConcurrentHashMap<String /* 服务名 */, List<RouteListener>> routeListenerMap = new ConcurrentHashMap<>();
    // 路由id对应的路由
    private final ConcurrentHashMap<String /* 路由id */, RouteDefinition> routeId2RouteMap = new ConcurrentHashMap<>();
    // 服务对应的路由
    private final ConcurrentHashMap<String /* 服务名 */, RouteDefinition> serviceName2RouteMap = new ConcurrentHashMap<>();
    // URI对应的路由
    private final ConcurrentHashMap<String /* URI路径 */, RouteDefinition> uri2RouteMap = new ConcurrentHashMap<>();
    // 服务
    private final ConcurrentHashMap<String /* 服务名 */, ServiceDefinition> serviceDefinitionMap = new ConcurrentHashMap<>();
    // 服务对应的实例
    private final ConcurrentHashMap<String /* 服务名 */, ConcurrentHashMap<String /* 实例id */, ServiceInstance>> serviceInstanceMap = new ConcurrentHashMap<>();

    /*********   单例   *********/
    private DynamicConfigManager() {
    }

    public static DynamicConfigManager getInstance() {
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
            uri2RouteMap.clear();
        }
        for (RouteDefinition route : routes) {
            if (route == null) continue;
            routeId2RouteMap.put(route.getId(), route);
            serviceName2RouteMap.put(route.getServiceName(), route);
            uri2RouteMap.put(route.getUri(), route);
        }
    }

    public RouteDefinition getRouteById(String id) {
        return routeId2RouteMap.get(id);
    }

    public RouteDefinition getRouteByServiceName(String serviceName) {
        return serviceName2RouteMap.get(serviceName);
    }

    public Set<Map.Entry<String, RouteDefinition>> getAllUriEntry() {
        return uri2RouteMap.entrySet();
    }

    /*********   服务   *********/
    public void updateService(ServiceDefinition serviceDefinition) {
        serviceDefinitionMap.put(serviceDefinition.getServiceName(), serviceDefinition);
    }

    public ServiceDefinition getServiceByName(String name) {
        return serviceDefinitionMap.get(name);
    }

    /*********   实例   *********/
    public void addServiceInstance(String serviceName, ServiceInstance instance) {
        serviceInstanceMap.computeIfAbsent(serviceName, k -> new ConcurrentHashMap<>()).put(instance.getInstanceId(), instance);
    }

    public void updateInstances(ServiceDefinition serviceDefinition, Set<ServiceInstance> newInstances) {
        ConcurrentHashMap<String, ServiceInstance> oldInstancesMap = serviceInstanceMap.computeIfAbsent(serviceDefinition.getServiceName(), k -> new ConcurrentHashMap<>());
        oldInstancesMap.clear();
        for (ServiceInstance newInstance : newInstances) {
            oldInstancesMap.put(newInstance.getInstanceId(), newInstance);
        }
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

    /*********   监听   *********/
    public void addRouteListener(String serviceName, RouteListener listener) {
        routeListenerMap.computeIfAbsent(serviceName, key -> new CopyOnWriteArrayList<>()).add(listener);
    }

    public void changeRoute(RouteDefinition routeDefinition) {
        List<RouteListener> routeListeners = routeListenerMap.get(routeDefinition.getServiceName());
        if (routeListeners == null || routeListeners.isEmpty()) return;
        for (RouteListener routeListener : routeListeners) {
            routeListener.changeOnRoute(routeDefinition);
        }
    }

}
