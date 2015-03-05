'use strict';

angular.module('polcoApp')
    .factory('NounTrans', function ($resource) {
        return $resource('api/nounTranss/:id', {}, {
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
