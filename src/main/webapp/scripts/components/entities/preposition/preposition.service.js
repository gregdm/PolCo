'use strict';

angular.module('polcoApp')
    .factory('Preposition', function ($resource) {
        return $resource('api/prepositions/:id', {}, {
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
