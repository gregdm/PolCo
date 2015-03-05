'use strict';

angular.module('polcoApp')
    .factory('PrepositionTrans', function ($resource) {
        return $resource('api/prepositionTranss/:id', {}, {
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
