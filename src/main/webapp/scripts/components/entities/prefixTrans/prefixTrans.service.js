'use strict';

angular.module('polcoApp')
    .factory('PrefixTrans', function ($resource) {
        return $resource('api/prefixTranss/:id', {}, {
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
