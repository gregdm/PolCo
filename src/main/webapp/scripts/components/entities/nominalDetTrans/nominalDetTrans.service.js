'use strict';

angular.module('polcoApp')
    .factory('NominalDetTrans', function ($resource) {
        return $resource('api/nominalDetTranss/:id', {}, {
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
