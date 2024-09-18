package com.grace.gateway.config.manager;

import com.grace.gateway.config.pojo.RouteDefinition;

public interface RouteListener {

    void changeOnRoute(RouteDefinition routeDefinition);

}
