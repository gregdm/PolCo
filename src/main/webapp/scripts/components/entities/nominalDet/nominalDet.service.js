'use strict';

angular.module('polcoApp')
    .factory('NominalDet', function ($resource) {
        return $resource('api/nominalDets/:id', {}, {
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
