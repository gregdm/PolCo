'use strict';

angular.module('polcoApp')
    .factory('ExpressionTrans', function ($resource) {
        return $resource('api/expressionTranss/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });