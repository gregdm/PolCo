'use strict';

angular.module('polcoApp')
    .factory('VerbTrans', function ($resource) {
        return $resource('api/verbTranss/:id', {}, {
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
